package composer;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import analysis.SimulationResultsWriter;
//import iotSys.broker.App;
//import iotSys.broker.JsonParser;
import iotSystemComponents.*;
import jmt.engine.simDispatcher.DispatcherJSIMschema;
import jmt.gui.common.definitions.CommonModel;
import jmt.gui.common.xml.XMLWriter;

public class QueueingNetworkComposer {
	 
	 public String composeNetwork(String inputFolder, int simulationDuration,double globalMessageSize) {
		 NgsiParser parser= new NgsiParser();
			parser.readSystemData("SystemSpecifications.st");
		 parser.readJSONLD(inputFolder);
		 
		 String priorityPolicy = parser.priorityPolicy;
		 
		 CommonModel jmtModel = new CommonModel();
		 IoTdeviceHandler iotDeviceHandler = new IoTdeviceHandler();
		 HashMap<String, IoTdevice> iotDevices = parser.iotDevices;
		 ArrayList<String> deviceNames = new ArrayList<>();
		 for(IoTdevice device : iotDevices.values()) {
			 deviceNames.add(device.deviceName);
		 }
		 iotDeviceHandler.addSources(jmtModel, deviceNames);
		 BrokerHandler brokerHandler = new BrokerHandler();
		 brokerHandler.addInputQueue(jmtModel, "input");
		 brokerHandler.addOutputQueue(jmtModel, "outputQueue");
		 
		 ApplicationHandler applicationHandler = new ApplicationHandler();
		 HashMap<String, Application> applications = parser.applications;
		 applicationHandler.addApplications(jmtModel, applications.values());
	 
		 VirtualSensorHandler virtualSensorHandler = new VirtualSensorHandler();
		 HashMap<String, VirtualSensor> virtualSensors = parser.virtualSensors;
		 virtualSensorHandler.addVirtualSensor(jmtModel, virtualSensors.values());
		 
		 TopicHandler topicHandler = new TopicHandler();
		 HashMap<String, Topic> topics = parser.topics;
		 ArrayList<String> topicNames = new ArrayList<String>();
		 for(Topic topic: topics.values()) {
			 topicNames.add(topic.topicName);
		 }
		 topicHandler.addTopicsForks(jmtModel, topicNames);
		 topicHandler.addTopicsJoin(jmtModel);
		 topicHandler.addTopicsSink(jmtModel);
		 topicHandler.addTopicsClassSwitches(jmtModel,topicNames);
		 
		 ClassHandler classHandler = new ClassHandler();
		 classHandler.addClassesForIoTdevices(jmtModel, iotDevices.values(),globalMessageSize);
		 classHandler.addClassesForVirtualSensors(jmtModel, virtualSensors.values(), globalMessageSize);
		 classHandler.addClassesForTopics(jmtModel, topics.values());
		 
		 DroppingHandler droppingHandler = new DroppingHandler();
		 droppingHandler.addDroppingSink(jmtModel);
		 
		 topicHandler.addSubTopics(jmtModel, topics.values(), applications, virtualSensors, priorityPolicy);
		 
		 HashMap<String, Subtopic> subtopics = topicHandler.subtopics;
		 PriorityHandler priorityHandler = new PriorityHandler();
		 priorityHandler.convertToJmtPriorities(jmtModel, applications, virtualSensors, subtopics);
		 priorityHandler.convertTopicPrioritiesToJmtPriorities(jmtModel, topics, subtopics);
		 
		 classHandler.addClassesForSubtopics(jmtModel, subtopics);
		 
		 topicHandler.setClassSwitchMatrix(jmtModel, topics);
		 topicHandler.setClassSwitchMatrixForSubtopics(jmtModel, subtopics);
		 topicHandler.setTopicsClassSwitchMatrix(jmtModel, subtopics);
		 topicHandler.setTopicsClassSwitchDroppingMatrix(jmtModel, subtopics);
		 
		 RoutingHandler routingHandler = new RoutingHandler();
		 routingHandler.setTopicsClassSwitchRouting(jmtModel, subtopics);
		 routingHandler.setApplicationsRouting(jmtModel, applications);
		 routingHandler.setInputQueueRouting(jmtModel, topics);
		 routingHandler.setOutputQueueRouting(jmtModel, subtopics);
		 routingHandler.setVirtualSensorsRouting(jmtModel, virtualSensors);
		 routingHandler.addDroppingRouting(jmtModel, subtopics, topicHandler.subtopicsClassSwitches,
				 parser.CHANNEL_LOSS_AN, parser.CHANNEL_LOSS_RT, parser.CHANNEL_LOSS_TS, parser.CHANNEL_LOSS_VS);
		 
		 NetworkResourcesManager networkManager = new NetworkResourcesManager(parser.systemBandwidth, parser.bandwidthPolicy, 
				 globalMessageSize);
		 networkManager.calculateDemands(parser);
		 networkManager.allocateResources();
		 
		 
		 LinkHandler linkHandler = new LinkHandler();
		 linkHandler.setConnections(jmtModel, parser, topicHandler.subtopicsClassSwitches);
		 
		 FiniteCapacityRegionHandler fcrHandler = new FiniteCapacityRegionHandler();
		 Object fcrObj = fcrHandler.setFiniteCapacityRegion(jmtModel, parser.BROKER_CAPACITY, subtopics);
		 
		 ServiceTimeHandler serviceTimeHandler = new ServiceTimeHandler();
		 serviceTimeHandler.setInputQueueServiceTime(jmtModel, topics);
		 serviceTimeHandler.setOutputQueueServiceTime(jmtModel, networkManager, subtopics);
		 serviceTimeHandler.setApplicationsServiceTime(jmtModel, applications);
		 serviceTimeHandler.setVirtualSensorsServiceTime(jmtModel, virtualSensors);
		 
		 PerformanceMetricsHandler performanceMetricsHandler = new PerformanceMetricsHandler();
		 double confInterval = 0.95;
		 double relErr = 0.05;
		 performanceMetricsHandler.setPerformanceMetrics(jmtModel, subtopics, fcrObj, confInterval, relErr);
		 
		 
		 
		 String jsimgFilePath = inputFolder+"\\simulation.jsimg";
		 File jsimFile = new File(jsimgFilePath);
		 XMLWriter.writeXML(jsimFile, jmtModel);
		 
		 return jsimgFilePath;
		 
		 
	 }
}
