package dataParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import guimodel.*;

public class DataParser {

	public static void addModeltoCsv(String filename, String model) {
		try {
			
			if(modelExists(filename, model.split(",")[0])) {
				deleteModel(filename, model.split(",")[0]);

				System.out.println(model.split(",")[0]);
			}
			FileWriter fileWriter = new FileWriter("data/"+filename+".csv", true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(model);
			bufferedWriter.newLine();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Boolean modelExists(String filename, String modelId) {
		try {

			FileReader fileReader = new FileReader("data/"+filename+".csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = bufferedReader.readLine();
			while (line != null) {
				String[] model = line.split(",");
				if(model[0].equals(modelId)) {
					System.out.println("exists");
					bufferedReader.close();
					return true;
				}
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("does not exist");
		return false;
	}

	public static void deleteModel(String filename, String modelId) {
		try {
			File inputFile = new File("data/"+filename+".csv");
			File tempFile = new File("data/"+filename+"Temp.csv");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			String currentLine;

			while((currentLine = reader.readLine()) != null) {
			    String trimmedLine = currentLine.trim();
			    if(trimmedLine.startsWith(modelId+",")) {
			    	continue;
			    }
			    writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close(); 
			reader.close(); 
			inputFile.delete();
			tempFile.renameTo(inputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		}
	public static <T> ArrayList<T> readModelFromCSv(String filename,Class T ) {
		ArrayList<T> items = new ArrayList<>();
		try {
			
			File dir = new File("data");
			if(dir.mkdirs()) {
				System.out.println("Created data dir");
			}
			File f = new File(dir, filename+".csv");
			if(!f.exists()) {
				System.out.println("file does not exist");
				f.createNewFile();
			}
			File file = new File("data\\"+filename+".csv");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				String[] data = line.split(",");
				if (T == Device.class){
				Device device = new Device();
				device.setDeviceId(data[0]);
				device.setDeviceName(data[1]);
				device.setPublishFrequency(Integer.parseInt(data[2]));
				device.setMessageSize(Double.parseDouble(data[3]));
				device.setDistribution(data[4]);
				String[] topics = data[5].split(";");
				List<Topic> deviceTopics = new ArrayList<>();
				for (String topic : topics) {
					deviceTopics.add(new Topic(topic));
				}
				device.setPublishesTo(deviceTopics);
				items.add((T) device);
				}
				else if (T == Topic.class){
					Topic topic = new Topic();
					topic.setId(data[0]);
					topic.setName(data[1]);
					String[] publishers = data[2].split(";");
					List<Device> topicPublishers = new ArrayList<>();
					for (String publisher : publishers) {
						topicPublishers.add(new Device(publisher));
					}
					topic.setPublishers(topicPublishers);
					String[] subscribers = data[3].split(";");
					List<Appl> topicSubscribers = new ArrayList<>();
					for (String subscriber : subscribers) {
						topicSubscribers.add(new Appl(subscriber));
					}
					topic.setSubscribers(topicSubscribers);
					items.add((T) topic);
				}
				else if(T==ApplicationCategory.class) {
					ApplicationCategory appCategory = new ApplicationCategory();
					appCategory.setCategoryId(data[0]);
					appCategory.setCategoryName(data[1]);
					items.add((T) appCategory);
				}
				else if(T==Appl.class) {
					Appl app = new Appl();
					app.setAppId(data[0]);
					app.setAppName(data[1]);
					app.setPriority(Integer.parseInt(data[2]));
					app.setProcessingRate(Double.parseDouble(data[3]));
					app.setApplicationCategory(new ApplicationCategory(data[4]));
					String[] topics = data[5].split(";");
					List<Topic> appTopics = new ArrayList<>();
					for (String topic : topics) {
						appTopics.add(new Topic(topic));
					}
					app.setSubscribesTo(appTopics);

					items.add((T) app);
				}
				else{
					System.err.println("Error: Class not found");
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return items;
	}
}
