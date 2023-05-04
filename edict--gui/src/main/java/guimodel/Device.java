package guimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
public class Device{
	
	private String id;
	private  String name;
	private int publishFrequency;
	private int messageSize;
	private String dataDistribution;
	private List<String> capturesObservation;
	
	public Device() {
		super();
	}

	public Device(String id, String name, int publishFrequency, int messageSize, String dataDistribution,
			List<String> capturesObservation) {
		super();
		this.id = id;
		this.name = name;
		this.publishFrequency = publishFrequency;
		this.messageSize = messageSize;
		this.dataDistribution = dataDistribution;
		this.capturesObservation = capturesObservation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPublishFrequency() {
		return publishFrequency;
	}

	public void setPublishFrequency(int publishFrequency) {
		this.publishFrequency = publishFrequency;
	}

	public int getMessageSize() {
		return messageSize;
	}

	public void setMessageSize(int messageSize) {
		this.messageSize = messageSize;
	}

	public String getDataDistribution() {
		return dataDistribution;
	}

	public void setDataDistribution(String dataDistribution) {
		this.dataDistribution = dataDistribution;
	}

	public List<String> getCapturesObservation() {
		return capturesObservation;
	}

	public void setCapturesObservation(List<String> capturesObservation) {
		this.capturesObservation = capturesObservation;
	}

	public Map toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", "Device");
		map.put("name", tempMap("Property",name));
		map.put("publishFrequency", tempMap("Property",publishFrequency));
		map.put("messageSize", tempMap("Property",messageSize));
		map.put("dataDistribution", tempMap("Property",dataDistribution));
		map.put("capturesObservation", tempMap("Relationship",capturesObservation));
		map.put("@context", "https://raw.githubusercontent.com/SAMSGBLab/edict--datamodels/main/context.jsonld");
		return map;
	}
	private Map<String, Object> tempMap(String type,Object value) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("type", type);
		if(type=="Relationship")
			map.put("object", value);
		else
			map.put("value", value);
		return map;
	}

	@Override
	public String toString() {
		StringBuilder device= new StringBuilder(id + "," + name + "," + publishFrequency + "," + messageSize + "," + dataDistribution + ",");
		if (capturesObservation == null) {
			device.append(";,");
			return device.toString();
		}
		for(String topic:capturesObservation) {
			device.append(topic).append(";");
		}
		return device.toString();
	}
}
