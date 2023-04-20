package guimodel;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
public class SystemSpecifications {

	private  int systemBandwidth;
	
	private  String bandwidthPolicy;
	
	private int commChannelLossRT;
	
	private int commChannelLossTS;
	
	private int commChannelLossVS;
	
	private int commChannelLossAN;
	
	private int brokerCapacity;
	
	private int simulationDuration;
	private String alias;
	private double globalMessageSize;
	
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

	public  void setSystemBandwidth(int systemBandwidth) {
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

	public int getSimulationDuration() {
		return simulationDuration;
	}

	public void setSimulationDuration(int simulationDuration) {
		this.simulationDuration = simulationDuration;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public double getGlobalMessageSize() {
		return globalMessageSize;
	}

	public void setGlobalMessageSize(double globalMessageSize) {
		this.globalMessageSize = globalMessageSize;
	}
	public boolean saveSystemSpecifications() {
		File file = new File("SystemSpecifications.st");
		if(file.exists()){
			file.delete();
		}
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			FileWriter fw = new FileWriter(file);
			fw.write("SystemBandwidth: "+systemBandwidth+"\n");
			fw.write("BandwidthPolicy: "+bandwidthPolicy+"\n");
			fw.write("CommChannelLossRT: "+commChannelLossRT+"\n");
			fw.write("CommChannelLossTS: "+commChannelLossTS+"\n");
			fw.write("CommChannelLossVS: "+commChannelLossVS+"\n");
			fw.write("CommChannelLossAN: "+commChannelLossAN+"\n");
			fw.write("BrokerCapacity: "+brokerCapacity+"\n");
			fw.write("SimulationDuration: "+simulationDuration+"\n");
			fw.write("Alias: "+alias+"\n");
			fw.write("GlobalMessageSize: "+globalMessageSize+"\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
			return true;

	}
	public boolean loadSystemSpecifications() {
		//load from file
		File file = new File("SystemSpecifications.st");
		if(!file.exists())
			return false;
		else{
			try {
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String line = br.readLine();
				while(line!=null){
					String[] parts = line.split(": ");
					if(parts[0].equals("SystemBandwidth"))
						systemBandwidth = Integer.parseInt(parts[1]);
					else if(parts[0].equals("BandwidthPolicy"))
						bandwidthPolicy = parts[1];
					else if(parts[0].equals("CommChannelLossRT"))
						commChannelLossRT = Integer.parseInt(parts[1]);
					else if(parts[0].equals("CommChannelLossTS"))
						commChannelLossTS = Integer.parseInt(parts[1]);
					else if(parts[0].equals("CommChannelLossVS"))
						commChannelLossVS = Integer.parseInt(parts[1]);
					else if(parts[0].equals("CommChannelLossAN"))
						commChannelLossAN = Integer.parseInt(parts[1]);
					else if(parts[0].equals("BrokerCapacity"))
						brokerCapacity = Integer.parseInt(parts[1]);
					else if(parts[0].equals("SimulationDuration"))
						simulationDuration = Integer.parseInt(parts[1]);
					else if(parts[0].equals("Alias"))
						alias = parts[1];
					else if(parts[0].equals("GlobalMessageSize"))
						globalMessageSize = Double.parseDouble(parts[1]);
					line = br.readLine();
				}
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
	
}
