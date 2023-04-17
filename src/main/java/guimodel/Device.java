package guimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Device{
	
	private String deviceId;

	private  String deviceName;
	
	private int publishFrequency;
	
	private double messageSize;
	
	private String distribution;
	
	private String deviceType;
	
	private List<Topic> publishesTo;

	public Device(String deviceId, String deviceName, int publishFrequency, double messageSize, String distribution,
			List<Topic> publishesTo) {
		super();
		this.deviceId = deviceId;
		this.deviceName =deviceName;
		this.publishFrequency = publishFrequency;
		this.messageSize = messageSize;
		this.distribution = distribution;
		this.publishesTo = publishesTo;
	}

	public Device(String deviceId, String deviceName, int publishFrequency, double messageSize, String distribution,
			String deviceType, List<Topic> publishesTo) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.publishFrequency = publishFrequency;
		this.messageSize = messageSize;
		this.distribution = distribution;
		this.deviceType = deviceType;
		this.publishesTo = publishesTo;
	}


	public Device(Device copy) {
		super();
		this.deviceId = copy.deviceId;
		this.deviceName =  copy.deviceName;
		this.publishFrequency =  copy.publishFrequency;
		this.messageSize =  copy.messageSize;
		this.distribution =  copy.distribution;
		this.deviceType =  copy.deviceType;
		this.publishesTo = new ArrayList<>();
		for(Topic tp : copy.publishesTo)
			this.publishesTo.add(tp);
	}

	public Device(String id) {
		super();
		this.deviceId=id;
		
	}
	public Device() {
		super();
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}



	public int getPublishFrequency() {
		return publishFrequency;
	}

	public void setPublishFrequency(int publishFrequency) {
		this.publishFrequency = publishFrequency;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getDistribution() {
		return distribution;
	}

	public void setDistribution(String distribution) {
		this.distribution = distribution;
	}

	public List<Topic> getPublishesTo() {
		return publishesTo;
	}

	public void setPublishesTo(List<Topic> publishesTo) {
		this.publishesTo = publishesTo;
	}

	public String getTopicsString() {
		String topics = "";
		for(Topic t : publishesTo) {
			topics += t.getName() + ";";
		}
		return topics;
	}

	public double getMessageSize() {
		return messageSize;
	}



	public void setMessageSize(double messageSize) {
		this.messageSize = messageSize;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		return Objects.equals(deviceId, other.deviceId);
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	
	

}
