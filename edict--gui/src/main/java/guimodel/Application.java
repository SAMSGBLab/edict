package guimodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
public class Application {
 enum ProcessingDistribution{
	 exponential,
	 deterministic 
}
	
	private String id;
	
	private String name;
	
	private String applicationCategory;
	
	private int priority;
	
	private int processingRate;
	private ProcessingDistribution processingDistribution;
	
	
	private List<String> recievesObservation;

	public Application() {
		super();
	}

	public Application(String appId, String appName, String applicationCategory, int priority,
			int processingRate, ProcessingDistribution processingDistribution, List<String> recievesObservation) {
		super();
		this.id = appId;
		this.name = appName;
		this.applicationCategory = applicationCategory;
		this.priority = priority;
		this.processingRate = processingRate;
		this.processingDistribution = processingDistribution;
		this.recievesObservation = recievesObservation;
	}

	public Application(String id) {
		super();
		this.id=id;
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

	public String getApplicationCategory() {
		return applicationCategory;
	}

	public void setApplicationCategory(String applicationCategory) {
		this.applicationCategory = applicationCategory;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public int getProcessingRate() {
		return processingRate;
	}

	public void setProcessingRate(int processingRate) {
		this.processingRate = processingRate;
	}

	public ProcessingDistribution getProcessingDistribution() {
		return processingDistribution;
	}

	public void setProcessingDistribution(ProcessingDistribution processingDistribution) {
		this.processingDistribution = processingDistribution;
	}

	public List<String> getRecievesObservation() {
		return recievesObservation;
	}

	public void setRecievesObservation(List<String> recievesObservation) {
		this.recievesObservation = recievesObservation;
	}
	
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", "Application");
		map.put("name", tempMap("Property",name));
		map.put("applicationCategory", tempMap("Relationship",applicationCategory));
		map.put("priority", tempMap("Property",priority));
		map.put("processingRate", tempMap("Property",processingRate));
		map.put("receivesObservation", tempMap("Relationship",recievesObservation));
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
