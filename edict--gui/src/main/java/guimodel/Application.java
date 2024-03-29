package guimodel;

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
	

	private int priority;
	
	private int processingRate;
	private ProcessingDistribution processingDistribution;
	private String applicationCategory;

	
	private List<String> receivesObservation;

	public Application() {
		super();
	}



	public Application(String id) {
		super();
		this.id=id;
	}

	public Application(String id, String name, int priority, int processingRate, String applicationCategory, List<String> receivesObservation) {
		this.id = id;
		this.name = name;
		this.priority = priority;
		this.processingRate = processingRate;
		this.applicationCategory = applicationCategory;
		this.receivesObservation = receivesObservation;
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

	public List<String> getReceivesObservation() {
		return receivesObservation;
	}

	public void setReceivesObservation(List<String> receivesObservation) {
		this.receivesObservation = receivesObservation;
	}
	public String toString() {
		StringBuilder app= new StringBuilder(id + "," + name + "," + priority + "," + processingRate +"," +applicationCategory+"," );
		if (receivesObservation.isEmpty()) {
			app.append(";");
		}
		for(String s: receivesObservation) {
			app.append(s).append(";");
		}

		return app.toString();
	}
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		map.put("type", "Application");
		map.put("name", tempMap("Property",name));
		map.put("applicationCategory", tempMap("Relationship",applicationCategory));
		map.put("priority", tempMap("Property",priority));
		map.put("processingRate", tempMap("Property",processingRate));
		map.put("receivesObservation", tempMap("Relationship", receivesObservation));
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
