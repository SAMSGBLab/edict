package guimodel;

import java.util.*;

public class Observation {
	
	private String id;
	private String name;

	
	
	private Set<String> isCapturedBy = new LinkedHashSet<>();
	
	private Set<String> isReceivedBy = new LinkedHashSet<>();
	public Observation() {
		super();
	}

	public Observation(String id) {
		this.id = "urn:ngsi-ld:edict:Observation:"+id;
	}

	public Observation(String id, String name, Set<String> isCapturedBy, Set<String> isReceivedBy) {
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

	public Set<String> getIsCapturedBy() {
		return isCapturedBy;
	}

	public void setIsCapturedBy(Set<String> isCapturedBy) {
		this.isCapturedBy = isCapturedBy;
	}

	public Set<String> getIsReceivedBy() {
		return isReceivedBy;
	}

	public void setIsReceivedBy(Set<String> isReceivedBy) {
		this.isReceivedBy = isReceivedBy;
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
		if(Objects.equals(type, "Relationship"))
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
