package guimodel;

import java.util.ArrayList;
import java.util.List;

public class Appl {

	private String appId;
	
	private String appName;
	
	private ApplicationCategory applicationCategory;
	
	private int priority;
	
	private double processingRate;
	
	private String processingDistribution;
	
	private List<Topic> subscribesTo;

	public Appl() {
		super();
	}

	public Appl(String appId, String appName, ApplicationCategory applicationCategory, int priority,
			double processingRate, String processingDistribution, List<Topic> subscribesTo) {
		super();
		this.appId = appId;
		this.appName = appName;
		this.applicationCategory = applicationCategory;
		this.priority = priority;
		this.processingRate = processingRate;
		this.processingDistribution = processingDistribution;
		this.subscribesTo = subscribesTo;
	}

	

	public Appl(Appl copy) {
		super();
		this.appId = copy.appId;
		this.appName =  copy.appName;
		this.applicationCategory =  copy.applicationCategory;
		this.priority =  copy.priority;
		this.processingRate =  copy.processingRate;
		this.processingDistribution =  copy.processingDistribution;
		this.subscribesTo = new ArrayList<>();
		for(Topic tp : copy.subscribesTo)
			this.subscribesTo.add(tp);
	}
	public Appl(String id) {
		super();
		this.appId=id;
	}
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public ApplicationCategory getApplicationCategory() {
		return applicationCategory;
	}

	public void setApplicationCategory(ApplicationCategory applicationCategory) {
		this.applicationCategory = applicationCategory;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public double getProcessingRate() {
		return processingRate;
	}

	public void setProcessingRate(double processingRate) {
		this.processingRate = processingRate;
	}

	public String getProcessingDistribution() {
		return processingDistribution;
	}

	public void setProcessingDistribution(String processingDistribution) {
		this.processingDistribution = processingDistribution;
	}

	public List<Topic> getSubscribesTo() {
		return subscribesTo;
	}

	public void setSubscribesTo(List<Topic> subscribesTo) {
		this.subscribesTo = subscribesTo;
	}
	
	
}
