package guimodel;

import java.util.ArrayList;
import java.util.List;

import home.Topic;

public class Broker {
	
	private String brokerId;
	
	private String brokerName;
	
	private int bufferSize;
	
	private int processingRate;
	
	private List<Topic> topics;

	public Broker(String brokerId, String brokerName, int bufferSize, int processingRate, List<Topic> topics) {
		super();
		this.brokerId = brokerId;
		this.brokerName = brokerName;
		this.bufferSize = bufferSize;
		this.processingRate = processingRate;
		this.topics = topics;
	}

	public Broker() {
		super();
		this.topics = new ArrayList<>();
	}

	public String getBrokerId() {
		return brokerId;
	}

	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	public String getBrokerName() {
		return brokerName;
	}

	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public int getProcessingRate() {
		return processingRate;
	}

	public void setProcessingRate(int processingRate) {
		this.processingRate = processingRate;
	}

	public List<Topic> getTopics() {
		return topics;
	}

	public void setTopics(List<Topic> topics) {
		this.topics = topics;
	}
	
	
//	"brokerId": "input",
//    "brokerName": "input",
//    "bufferSize": 10000,
//    "processingRate": 100000000,
//    "topics": ["topic_topic1", "topic_topic2", "topic_topic3", "topic_topic4", "topic_topic5", "topic_topic6", "topic_topic7",
//		"topic_topic8", "topic_topic9"]
	
}
