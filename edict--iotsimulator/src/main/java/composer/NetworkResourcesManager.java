package composer;

import iotSystemComponents.Application;
import iotSystemComponents.ApplicationCategory;
import iotSystemComponents.IoTdevice;
import iotSystemComponents.Topic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Flow;

public class NetworkResourcesManager {

	public String allocationPolicy;
	public double totalResources;
	public double allocatedBw_AN;
	public double allocatedBw_RT;
	public double allocatedBw_TS;
	public double allocatedBw_VS;
	
	public double globalMessageSize;
	public double[] demands ;
	
	public NetworkResourcesManager(double totalResources, String allocationPolicy, double globalMessageSize) {
		this.totalResources = totalResources/globalMessageSize;
		this.allocationPolicy = allocationPolicy;
		this.globalMessageSize = globalMessageSize;
		this.demands = new double[]{0, 0, 0, 0};
	}
	
	public void allocateResources() {
		if (allocationPolicy.equals("none")) {
			allocatedBw_AN = totalResources;
			allocatedBw_RT = totalResources;
			allocatedBw_TS = totalResources;
			allocatedBw_VS = totalResources;
		}
		
		else if (allocationPolicy.equals("shared")) {
			allocatedBw_AN = totalResources / 4;
			allocatedBw_RT = totalResources / 4;
			allocatedBw_TS = totalResources / 4;
			allocatedBw_VS = totalResources / 4;
		}
		else if (allocationPolicy.equals("max_min")) {
			double[] allocatedBw = new double[]{0, 0, 0, 0};
			double remainingResources = totalResources;
			int remainingUsers =4;
			for (int i = 0; i < 4; i++) {
				if (demands[i]==0) {
					remainingUsers--;
				}
			}
			while (remainingResources > 0 && remainingUsers > 0) {
				double minDemand = Double.MAX_VALUE;
				for (int i = 0; i < 4; i++) {
					if (demands[i] > 0 && demands[i] < minDemand) {
						minDemand = demands[i];
					}
				}
				double fairShare = remainingResources / remainingUsers;
				if (minDemand <= fairShare) {
					for (int i = 0; i < 4; i++) {
						if (demands[i] > 0 && demands[i] <= minDemand) {
							allocatedBw[i] += demands[i];
							remainingResources -= demands[i];
							demands[i] = 0;
							remainingUsers--;
						}
					}
				} else {
					for (int i = 0; i < 4; i++) {
						if (demands[i] > 0) {
							allocatedBw[i] += fairShare;
							remainingResources -= fairShare;
							demands[i] -= fairShare;
						}
					}
				}
			}

			allocatedBw_RT = allocatedBw[0];
			allocatedBw_AN = allocatedBw[1];
			allocatedBw_VS = allocatedBw[2];
			allocatedBw_TS = allocatedBw[3];

		}

	}
	public void calculateDemands(NgsiParser ngsiParser) {
		HashMap<String, IoTdevice> devices = ngsiParser.iotDevices;
		HashMap<String, Application> applications = ngsiParser.applications;
		HashMap<String, Topic> topics = ngsiParser.topics;
		HashMap<String,Double> topicLoad = new HashMap<>();
		HashMap<String,Double> categoryLoad = new HashMap<>();
		categoryLoad.put("RT",0.0);
		categoryLoad.put("AN",0.0);
		categoryLoad.put("VS",0.0);
		categoryLoad.put("TS",0.0);
		for(Topic topic:topics.values()){
			topicLoad.put(topic.topicName,0.0);
		}
		for(Application application:applications.values()){
			categoryLoad.put(application.applicationCategory,0.0);
		}
		for(IoTdevice ioTdevice:devices.values()){
			double deviceLoad = ioTdevice.messageSize*ioTdevice.publishFrequency;
			for(String topicName:ioTdevice.publishedTopics){
				topicLoad.put(topicName,topicLoad.get(topicName)+deviceLoad);
			}
		}
		for(Application application:applications.values()){
			for (String topicName:application.subscribedTopics){
				categoryLoad.put(application.applicationCategory,categoryLoad.get(application.applicationCategory)+topicLoad.get(topicName));
			}
		}
		categoryLoad.replaceAll((c, v) -> categoryLoad.get(c) / globalMessageSize);

		demands=categoryLoad.values().stream().mapToDouble(Double::doubleValue).toArray();

	}
}
