package guimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Observation {
	
	private String id;
	private String name;

	
	
	private List<String> isCapturedBy = new ArrayList<>(); //publishers
	
	private List<String> isReceivedBy = new ArrayList<>(); //subscribers
	public Observation() {
		super();
	}

	public Observation(String id) {
		this.id = "urn:ngsi-ld:edict:Observation:"+id;
	}

	public Observation(String id, String name, List<String> isCapturedBy, List<String> isReceivedBy) {
		this.id = id;
		this.name = name;
		this.isCapturedBy = isCapturedBy;
		this.isReceivedBy = isReceivedBy;
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
	public List<String> getIsCapturedBy() {
		return isCapturedBy;
	}
	public void setIsCapturedBy(List<String> isCapturedBy) {
		this.isCapturedBy = isCapturedBy;
	}
	public List<String> getIsReceivedBy() {
		return isReceivedBy;
	}
	public void setIsReceivedBy(List<String> isRecievedBy) {
		this.isReceivedBy = isRecievedBy;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", "Observation");
		map.put("name",tempMap("Property",name));
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

	@Override
	public String toString() {
		StringBuilder ob= new StringBuilder(id + ',' + name + ',');
		if(isCapturedBy.isEmpty()){
			ob.append(";");
		}
		for(String device:isCapturedBy){
			ob.append(device).append(";");
		}
		ob.append(',');
		if(isReceivedBy.isEmpty()){
			ob.append(";");
		}
		for(String app:isReceivedBy){
			ob.append(app).append(";");
		}
		ob.append(',');
		return ob.toString();


	}
}
