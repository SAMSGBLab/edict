package model;

public class DeviceType {

	private String deviceTypeId; 
	private String deviceTypeName;
	
	
	public DeviceType() {
		super();
	}
	
	public DeviceType(String deviceTypeId, String deviceTypeName) {
		super();
		this.deviceTypeId = deviceTypeId;
		this.deviceTypeName = deviceTypeName;
	}

	public String getDeviceTypeId() {
		return deviceTypeId;
	}
	public void setDeviceTypeId(String deviceTypeId) {
		this.deviceTypeId = deviceTypeId;
	}
	public String getDeviceTypeName() {
		return deviceTypeName;
	}
	public void setDeviceTypeName(String deviceTypeName) {
		this.deviceTypeName = deviceTypeName;
	}
	
	
}
