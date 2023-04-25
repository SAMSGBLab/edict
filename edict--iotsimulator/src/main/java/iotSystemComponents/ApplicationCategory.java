package iotSystemComponents;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

public class ApplicationCategory {

//	@JsonProperty("id")
	public String categoryId;
	@JsonProperty("name")
	public String categoryName;
	public double processingTime;
	public ApplicationCategory() {
	}
	public ApplicationCategory(String categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	@JsonProperty("name")
	private void unpackName(Map<String, Object> nameMap) {
		if (nameMap.containsKey("value")) {
			categoryName = (String) nameMap.get("value");
		}
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public double getProcessingTime() {
		return processingTime;
	}
	public void setProcessingTime(double processingTime) {
		this.processingTime = processingTime;
	}
}
