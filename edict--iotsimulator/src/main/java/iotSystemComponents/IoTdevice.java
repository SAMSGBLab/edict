package iotSystemComponents;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Map;

public class IoTdevice {

//	@JsonProperty("id")
	public String deviceId;
	public String deviceName;
	public double publishFrequency;  //in msgs/sec
	public double messageSize; //in bytes
	public String distribution;
	public ArrayList<String> publishedTopics;
	public IoTdevice() {}
	
	public IoTdevice(String deviceId, String deviceName, double publishFrequency, double messageSize, String distribution, ArrayList<String> publishedTopics) {
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.publishFrequency =publishFrequency;
		this.messageSize = messageSize;
		this.distribution = distribution;
		this.publishedTopics = (ArrayList<String>) publishedTopics.clone();
		
	}
	@JsonProperty("name")
	private void unpackName(Map<String, Object> nameMap) {
		if (nameMap.containsKey("value")) {
			deviceName = (String) nameMap.get("value");
		}
	}

	@JsonProperty("publishFrequency")
	private void unpackPublishFrequency(Map<String, Object> publishFrequencyMap) {
		if (publishFrequencyMap.containsKey("value")) {
			publishFrequency = (double) ((Integer)publishFrequencyMap.get("value"));
		}
	}
	@JsonProperty("messageSize")
	private void unpackMessageSize(Map<String, Object> messageSizeMap) {
		if (messageSizeMap.containsKey("value")) {
			messageSize = (double)((Integer) messageSizeMap.get("value"));
		}
	}
	@JsonProperty("dataDistribution")
	private void unpackDistribution(Map<String, Object> distributionMap) {
		if (distributionMap.containsKey("value")) {
			distribution = (String) distributionMap.get("value");
		}
	}
	@JsonProperty("capturesObservation")
	private void unpackPublishedTopics(Map<String, Object> publishedTopicsMap) {
		if (publishedTopicsMap.containsKey("object")){
			publishedTopics = (ArrayList<String>) publishedTopicsMap.get("object");
		}
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public double getPublishFrequency() {
		return publishFrequency;
	}

	public void setPublishFrequency(double publishFrequency) {
		this.publishFrequency = publishFrequency;
	}

	public double getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(double messageSize) {
		this.messageSize = messageSize;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public ArrayList<String> getPublishedTopics() {
		return publishedTopics;
	}

	public void setPublishedTopics(ArrayList<String> publishedTopics) {
		this.publishedTopics = publishedTopics;
	}

}
