package iotSystemComponents;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;

public class Application implements Subscriber{

//	@JsonProperty("id")
	public String applicationId;

	public String applicationName;
	public String applicationCategory;
	public int processingRate;
	public String processingDistribution;
	public ArrayList<String> subscribedTopics;
	public int priority;		
	public int jmtPriority;			

	public Application(){

	}
	public Application (String applicationId, String applicationName, String applicationCategory, int processingRate, 
			String processingDistribution, ArrayList<String>subscribedTopics, int priority) {
		this.applicationId = applicationId;
		this.applicationName = applicationName;
		this.applicationCategory = applicationCategory;
		this.processingRate = processingRate;
		this.processingDistribution = processingDistribution;
		this.subscribedTopics = (ArrayList<String>) subscribedTopics.clone();
		this.priority = priority;
		this.jmtPriority = 0;
	}

	@JsonProperty("name")
	private void unpackName(Map<String, Object> nameMap) {
		if (nameMap.containsKey("value")) {
			applicationName = (String) nameMap.get("value");
		}
	}

	@JsonProperty("priority")
	private void unpackPriority(Map<String, Object> priorityMap) {
		if (priorityMap.containsKey("value")) {
			priority = ((Integer) priorityMap.get("value")).intValue();
		}
	}

	@JsonProperty("processingRate")
	private void unpackProcessingRate(Map<String, Object> processingRateMap) {
		if (processingRateMap.containsKey("value")) {
			processingRate = ((Integer) processingRateMap.get("value")).intValue();
		}
	}
	@JsonProperty("processingDistribution")
	private void unpackProcessingDistribution(Map<String, Object> processingDistributionMap) {
		if (processingDistributionMap.containsKey("value")) {
			processingDistribution = (String) processingDistributionMap.get("value");
		}
	}
	@JsonProperty("applicationCategory")
	private void unpackApplicationCategory(Map<String, Object> categoryMap) {
		if (categoryMap.containsKey("object")) {
			applicationCategory = (String) categoryMap.get("object");
		}
	}
	@JsonProperty("receivesObservation")
	private void unpackSubscribedTopics(Map<String, Object> receivesObservationMap) {
		if (receivesObservationMap.containsKey("object")) {
			Object object = receivesObservationMap.get("object");
			if (object instanceof ArrayList) {
				subscribedTopics = (ArrayList<String>) object;
			}
		}
	}
	public String getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(String applicationId) {
		this.applicationId = applicationId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getApplicationCategory() {
		return applicationCategory;
	}
	public void setApplicationCategory(String applicationCategory) {
		this.applicationCategory = applicationCategory;
	}
	public int getProcessingRate() {
		return processingRate;
	}
	public void setProcessingRate(int processingRate) {
		this.processingRate = processingRate;
	}
	public String getProcessingDistribution() {
		return processingDistribution;
	}
	public void setProcessingDistribution(String processingDistribution) {
		this.processingDistribution = processingDistribution;
	}
	public ArrayList<String> getSubscribedTopics() {
		return subscribedTopics;
	}
	public void setSubscribedTopics(ArrayList<String> subscribedTopics) {
		this.subscribedTopics = subscribedTopics;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public int getJmtPriority() {
		return jmtPriority;
	}
	public void setJmtPriority(int jmtPriority) {
		this.jmtPriority = jmtPriority;
	}
}
