package home;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import guimodel.Appl;
import guimodel.Device;

public class Topic {
	
	private String id;
	private String name;
	
	private List<Device> publishers = new ArrayList<>();
	private List<Appl> subscribers = new ArrayList<>();
	
	public Topic(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Topic(String id, String name, List<Device> publishers, List<Appl> subscribers) {
		super();
		this.id = id;
		this.name = name;
		this.publishers = publishers;
		this.subscribers = subscribers;
	}



	public Topic() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Topic other = (Topic) obj;
		return Objects.equals(id, other.id);
	}
	
	

	@Override
	public String toString() {
		return name;
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

	public List<Device> getPublishers() {
		return publishers;
	}

	public void setPublishers(List<Device> publishers) {
		this.publishers = publishers;
	}



	public List<Appl> getSubscribers() {
		return subscribers;
	}



	public void setSubscribers(List<Appl> subscribers) {
		this.subscribers = subscribers;
	}
	

}
