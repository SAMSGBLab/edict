package guimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
public class Observation {
	
	private String id;
	private String name;
	
	private int priority;
	
	
	private List<String> isCapturedBy = new ArrayList<>(); //publishers
	
	private List<String> isReceivedBy = new ArrayList<>(); //subscribers
	public Observation() {
		super();
	}
	public Observation(String id, String name, int priority, List<String> isCapturedBy, List<String> isRecievedBy) {
		super();
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.isCapturedBy = isCapturedBy;
		this.isReceivedBy = isRecievedBy;
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
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public List<String> getIsCapturedBy() {
		return isCapturedBy;
	}
	public void setIsCapturedBy(List<String> isCapturedBy) {
		this.isCapturedBy = isCapturedBy;
	}
	public List<String> getIsRecievedBy() {
		return isReceivedBy;
	}
	public void setIsRecievedBy(List<String> isRecievedBy) {
		this.isReceivedBy = isRecievedBy;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", "Observation");
		map.put("name",tempMap("Property",name));
		map.put("priority", tempMap("Property",priority));
		map.put("isCapturedBy", tempMap("Relationship",isCapturedBy));
		map.put("isReceivedBy", tempMap("Relationship",isReceivedBy));
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
}
