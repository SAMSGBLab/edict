package guimodel;

public class SystemSpecifications {

	private int systemBandwidth;
	
	private String bandwidthPolicy;
	
	private int commChannelLossRT;
	
	private int commChannelLossTS;
	
	private int commChannelLossVS;
	
	private int commChannelLossAN;
	
	private int brokerCapacity;
	
	public SystemSpecifications() {
		super();
	}

	public SystemSpecifications(int systemBandwidth, String bandwidthPolicy, int commChannelLossRT,
			int commChannelLossTS, int commChannelLossVS, int commChannelLossAN, int brokerCapacity) {
		super();
		this.systemBandwidth = systemBandwidth;
		this.bandwidthPolicy = bandwidthPolicy;
		this.commChannelLossRT = commChannelLossRT;
		this.commChannelLossTS = commChannelLossTS;
		this.commChannelLossVS = commChannelLossVS;
		this.commChannelLossAN = commChannelLossAN;
		this.brokerCapacity = brokerCapacity;
	}

	public int getSystemBandwidth() {
		return systemBandwidth;
	}

	public void setSystemBandwidth(int systemBandwidth) {
		this.systemBandwidth = systemBandwidth;
	}

	public String getBandwidthPolicy() {
		return bandwidthPolicy;
	}

	public void setBandwidthPolicy(String bandwidthPolicy) {
		this.bandwidthPolicy = bandwidthPolicy;
	}

	public int getCommChannelLossRT() {
		return commChannelLossRT;
	}

	public void setCommChannelLossRT(int commChannelLossRT) {
		this.commChannelLossRT = commChannelLossRT;
	}

	public int getCommChannelLossTS() {
		return commChannelLossTS;
	}

	public void setCommChannelLossTS(int commChannelLossTS) {
		this.commChannelLossTS = commChannelLossTS;
	}

	public int getCommChannelLossVS() {
		return commChannelLossVS;
	}

	public void setCommChannelLossVS(int commChannelLossVS) {
		this.commChannelLossVS = commChannelLossVS;
	}

	public int getCommChannelLossAN() {
		return commChannelLossAN;
	}

	public void setCommChannelLossAN(int commChannelLossAN) {
		this.commChannelLossAN = commChannelLossAN;
	}

	public int getBrokerCapacity() {
		return brokerCapacity;
	}

	public void setBrokerCapacity(int brokerCapacity) {
		this.brokerCapacity = brokerCapacity;
	}
	
	
}
