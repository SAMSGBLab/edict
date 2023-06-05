package composer;

import iotSystemComponents.Application;
import iotSystemComponents.Subtopic;
import iotSystemComponents.Topic;
import iotSystemComponents.VirtualSensor;
import jmt.gui.common.CommonConstants;
import jmt.gui.common.definitions.CommonModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class TopicHandler {

    HashMap<String, String> subtopicsClassSwitches = new HashMap<String, String>();
    HashMap<String, Subtopic> subtopics = new HashMap<String, Subtopic>();
    HashMap<String, ArrayList<String>> subtopicsOfTopic = new HashMap<String, ArrayList<String>>();
    
    public void addTopicsJoin(CommonModel jmtModel) {
    	jmtModel.addStation("topics_join", CommonConstants.STATION_TYPE_JOIN);
    }
    
    public void addTopicsSink(CommonModel jmtModel) {
    	jmtModel.addStation("topics_sink", CommonConstants.STATION_TYPE_SINK);
    }
    
    public void addTopicsForks(CommonModel jmtModel, ArrayList<String> topicsNames) {
    	for (String topicName : topicsNames) {
    		jmtModel.addStation(topicName + "_fork", CommonConstants.STATION_TYPE_FORK);
    	}
    }
    	
    public void addTopicsClassSwitches(CommonModel jmtModel, ArrayList<String> topicsNames) {
    	for (String topicName : topicsNames) {
    		jmtModel.addStation(topicName + "_switch", CommonConstants.STATION_TYPE_CLASSSWITCH);
    	}
    	jmtModel.addStation("topics_class_switch", CommonConstants.STATION_TYPE_CLASSSWITCH);
    }
       
    public void addSubTopics(CommonModel jmtModel, Collection<Topic> topics, HashMap<String, Application> applications, HashMap<String, VirtualSensor> virtualSensors, String priorityPolicy) {
		for (Topic topic : topics) {
			ArrayList<String> subtopicsList = new ArrayList<String>();
			int priority = 0;
			int jmtPriority = 0;
			String applicationCategory = "";
			for (String subscriberName : topic.subscribers) {
				if (applications.containsKey(subscriberName)) {
					Application application = applications.get(subscriberName);
					priority = application.priority;
					jmtPriority = application.jmtPriority;
					applicationCategory = application.applicationCategory;
				}
				else {
					VirtualSensor virtualSensor = virtualSensors.get(subscriberName);
					priority = virtualSensor.priority;
					jmtPriority = virtualSensor.jmtPriority;
					applicationCategory = virtualSensor.applicationCategory;
				}
				String subTopicName = "";
				if (priorityPolicy.equals("apps")) {
					subTopicName = topic.topicName + "_" + subscriberName + "_" + applicationCategory + new Integer(priority).toString();

				}
					
				else if (priorityPolicy.equals("topics")) {
					subTopicName = topic.topicName + "_" + subscriberName + "_" + applicationCategory + new Integer(topic.topicPriority).toString();
					priority = topic.topicPriority;
					jmtPriority = topic.jmtPriority;
				}
				String classSwitchName = subTopicName + "_switch";  //topic_app_cat_prio
				subtopicsClassSwitches.put(subTopicName, classSwitchName);
				jmtModel.addStation(classSwitchName, CommonConstants.STATION_TYPE_CLASSSWITCH);
				topic.subTopics.add(subTopicName);
				
				Subtopic subTopic = new Subtopic(subTopicName, topic.topicName , subscriberName, applicationCategory, priority, jmtPriority);
				subtopics.put(subTopicName, subTopic);
				subtopicsList.add(subTopicName);
			}
		subtopicsOfTopic.put(topic.topicName, subtopicsList);	
		}
	}
    
    public void setClassSwitchMatrix(CommonModel jmtModel, HashMap<String, Topic> topics) {
    	for (Topic topic : topics.values()) {
        	Object classSwitch = jmtModel.getStationByName(topic.topicName + "_switch");
			for (String publisher : topic.publishers) {
        		jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(publisher + "_class"), jmtModel.getClassByName(publisher + "_class"), 0.0f);
        		jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(publisher + "_class"), jmtModel.getClassByName(topic.topicName + "_class"), 1.0f);
        	}
    	}
    }
    
    public void setClassSwitchMatrixForSubtopics(CommonModel jmtModel, HashMap<String, Subtopic> subtopics) {
    	for (Subtopic subtopic : subtopics.values()) {
        	Object classSwitch = jmtModel.getStationByName(subtopicsClassSwitches.get(subtopic.name));
        	jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(subtopic.parentTopicName + "_class"), jmtModel.getClassByName(subtopic.parentTopicName + "_class"), 0.0f);
        	jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(subtopic.parentTopicName + "_class"), jmtModel.getClassByName(subtopic.name + "_class"), 1.0f);
        }
    }
    
	public void setTopicsClassSwitchMatrix(CommonModel jmtModel, HashMap<String, Subtopic> subtopics) {
		for (Subtopic subtopic : subtopics.values()) {
			Object classSwitch = jmtModel.getStationByName("topics_class_switch");
			jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(subtopic.name + "_class"), jmtModel.getClassByName(subtopic.name + "_class"), 0.0f);
			jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(subtopic.name + "_class"), jmtModel.getClassByName(subtopic.parentTopicName + "_class"), 1.0f);
		}
	}
	
	public void setTopicsClassSwitchDroppingMatrix(CommonModel jmtModel, HashMap<String, Subtopic> subtopics) {
		for (Subtopic subtopic : subtopics.values()) {
			Object classSwitch = jmtModel.getStationByName("topics_class_switch_dropping");
			jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(subtopic.name + "_class"), jmtModel.getClassByName(subtopic.name + "_class"), 0.0f);
			jmtModel.setClassSwitchMatrix(classSwitch, jmtModel.getClassByName(subtopic.name + "_class"), jmtModel.getClassByName(subtopic.parentTopicName + "_class"), 1.0f);
		}
	}
	
}
