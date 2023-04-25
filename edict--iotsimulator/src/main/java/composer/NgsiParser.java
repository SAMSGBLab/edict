package composer;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import iotSystemComponents.Actuator;
import iotSystemComponents.Application;
import iotSystemComponents.ApplicationCategory;
import iotSystemComponents.IoTdevice;
import iotSystemComponents.Topic;
import iotSystemComponents.VirtualSensor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class NgsiParser {
	public final int CHANNEL_LOSS_AN=0;
	public final int CHANNEL_LOSS_RT=0;
	public final int CHANNEL_LOSS_TS=0;
	public final int CHANNEL_LOSS_VS=0;
	public final int systemBandwidth=230;
	public final String bandwidthPolicy="none";
	public final int BROKER_CAPACITY=-1;
	public static String priorityPolicy = "apps";
	
    public  HashMap<String,ApplicationCategory> applicationCategories;
    public  HashMap<String,Application> applications;
    public  HashMap<String,Topic> topics;
    public  HashMap<String,IoTdevice> iotDevices ;
    public  HashMap<String,VirtualSensor> virtualSensors ;
    public  HashMap<String,Actuator> actuators;
    public HashMap<String, String> idName;
    private  <T> T NGSI_parse(File file, Class<T> cl) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(file, cl);

    }

    public void readJSONLD(String folderPath) {
        File[] folders = new File(folderPath).listFiles();
        for (File folder : folders) {
        	if (!folder.isDirectory()) continue;
            File[] files = folder.listFiles();
            for (File file : files) {
                
                String id = null;
                ObjectNode node;
				try {
					node = new ObjectMapper().readValue(file, ObjectNode.class);
				}
                catch (Exception e) {
                    System.out.println("Error parsing file: " + file.getName());
                    continue;
                }
                final JsonNode idNode = node.get("id");
                final JsonNode nameNode = node.get("name");
                if (idNode != null) {
                    id = idNode.asText();
                    idName.put(id, nameNode.get("value").asText());
                }

                try {
                    if (folder.getName().equals("applicationCategories")) {
                        applicationCategories.put( id, NGSI_parse(file, ApplicationCategory.class));
                    } else if (folder.getName().equals("applications")) {
                        applications.put( id, NGSI_parse(file, Application.class));
                    } else if (folder.getName().equals("devices")) {
                        iotDevices.put( id, NGSI_parse(file, IoTdevice.class));
                    } else if (folder.getName().equals("observations")) {
                        topics.put( id, NGSI_parse(file, Topic.class));
                    } else if (folder.getName().equals("sensors")) {
                        virtualSensors.put( id, NGSI_parse(file, VirtualSensor.class));
                    } 
                    else {
                        System.out.println("Unknown model: " + folder.getName());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }
            }
        updateData();
        }
    private void updateData(){
        for(Topic t:topics.values()){
            for(String publisherId:t.getPublishers()){
                
                t.getPublishers().set(t.getPublishers().indexOf(publisherId),iotDevices.get(publisherId).getDeviceName());
            }    
            for(String subscriberId:t.getSubscribers()){
            	t.getSubscribers().set(t.getSubscribers().indexOf(subscriberId),applications.get(subscriberId).getApplicationName());;
            }
        }
        for (Application a:applications.values()){
            for(String topicId:a.getSubscribedTopics()){
                a.getSubscribedTopics().set(a.getSubscribedTopics().indexOf(topicId),topics.get(topicId).getTopicName());
            }
            a.setApplicationCategory(applicationCategories.get(a.getApplicationCategory()).getCategoryName());
        }
        for (IoTdevice d:iotDevices.values()){
            for(String topicId:d.getPublishedTopics()){
                d.getPublishedTopics().set(d.getPublishedTopics().indexOf(topicId),topics.get(topicId).getTopicName());
            }
        }
        for(String id: idName.keySet()){
            if(applicationCategories.containsKey(id)){
                applicationCategories.put(idName.get(id), applicationCategories.get(id));
                applicationCategories.remove(id);
            }
            if(applications.containsKey(id)){
                applications.put(idName.get(id), applications.get(id));
               applications.remove(id);
            }
            if(topics.containsKey(id)){
                topics.put(idName.get(id), topics.get(id));
                topics.remove(id);
            }
            if(iotDevices.containsKey(id)){
                iotDevices.put(idName.get(id), iotDevices.get(id));
                iotDevices.remove(id);
            }
            if(virtualSensors.containsKey(id)){
                virtualSensors.put(idName.get(id), virtualSensors.get(id));
                virtualSensors.remove(id);
            }
        }
        
    }
    public NgsiParser() {
    	applicationCategories = new HashMap<String, ApplicationCategory>();
    	applications = new HashMap<String, Application>();
    	topics = new HashMap<String, Topic>();
    	iotDevices = new  HashMap<String, IoTdevice>();
    	virtualSensors = new  HashMap<String, VirtualSensor>();
    	actuators=new HashMap<String, Actuator>();
        idName = new HashMap<String, String>();
    }
}
