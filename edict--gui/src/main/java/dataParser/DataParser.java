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
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import guimodel.*;
import modelingEntities.ApplicationEntity;
import modelingEntities.BrokerEntity;
import modelingEntities.DeviceEntity;

public class DataParser {


    public static void addModeltoCsv(String filename, String model) {
        try {

            if (modelExists(filename, model.split(",")[0])) {
                deleteObject(filename, model.split(",")[0]);
            }
            FileWriter fileWriter = new FileWriter("data/" + filename + ".csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(model);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addEntityToCsv(String filename, String entity) {
        try {
            if (entityExists(filename, entity.split(",")[0])) {
                deleteObject(filename, entity.split(",")[0]);
            }
            FileWriter fileWriter = new FileWriter("data/" + filename + ".csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(entity);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean modelExists(String filename, String modelId) {
        try {

            FileReader fileReader = new FileReader("data/" + filename + ".csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] model = line.split(",");
                if (model[0].equals(modelId)) {
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
        return false;
    }

    public static Boolean entityExists(String filename, String entityId) {
        try {

            FileReader fileReader = new FileReader("data/" + filename + ".csv");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] entity = line.split(",");
                if (entity[0].equals(entityId)) {
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
        return false;
    }



    public static void deleteObject(String filename, String modelId) {
        try {
            File inputFile = new File("data/" + filename + ".csv");
            File tempFile = new File("data/" + filename + "Temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if (trimmedLine.startsWith(modelId + ",")) {
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


    public static <T> ArrayList<T> readModelFromCSv(String filename, Class T) {
        ArrayList<T> items = new ArrayList<>();
        try {
            File dir = new File("data");
            if (dir.mkdirs()) {
                System.out.println("Created data dir");
            }
            File f = new File(dir, filename + ".csv");
            if (!f.exists()) {
                System.out.println("file does not exist");
                f.createNewFile();
            }
            File file = new File("data/" + filename + ".csv");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                String[] data = line.split(",");
                if (T == Device.class) {
                    Device device = new Device();
                    device.setId(data[0]);
                    device.setName(data[1]);
                    device.setPublishFrequency(Integer.parseInt(data[2]));
                    device.setMessageSize(Integer.parseInt(data[3]));
                    device.setDataDistribution(data[4]);
                    String[] topics = data[5].split(";");
                    List<String> deviceTopics = new ArrayList<>();
                    for (String topic : topics) {
                        deviceTopics.add(topic);
                    }
                    device.setCapturesObservation(deviceTopics);
                    items.add((T) device);
                } else if (T == Observation.class) {
                    Observation topic = new Observation();
                    topic.setId(data[0]);
                    topic.setName(data[1]);
                    String[] publishers = data[2].split(";");
                    List<String> topicPublishers = new ArrayList<>();
                    for (String publisher : publishers) {
                        topicPublishers.add(publisher);
                    }
                    topic.setIsCapturedBy(topicPublishers);
                    String[] subscribers = data[3].split(";");
                    List<String> topicSubscribers = new ArrayList<>();
                    for (String subscriber : subscribers) {
                        topicSubscribers.add(subscriber);
                    }
                    topic.setIsRecievedBy(topicSubscribers);
                    items.add((T) topic);
                } else if (T == ApplicationCategory.class) {
                    ApplicationCategory appCategory = new ApplicationCategory();
                    appCategory.setId(data[0]);
                    appCategory.setName(data[1]);
                    items.add((T) appCategory);
                } else if (T == Application.class) {
                    Application app = new Application();
                    app.setId(data[0]);
                    app.setName(data[1]);
                    app.setPriority(Integer.parseInt(data[2]));
                    app.setProcessingRate(Integer.parseInt(data[3]));
                    app.setApplicationCategory(data[4]);
                    String[] topics = data[5].split(";");
                    List<String> appTopics = new ArrayList<>();
                    for (String topic : topics) {
                        appTopics.add(topic);
                    }
                    app.setRecievesObservation(appTopics);

                    items.add((T) app);
                } else {
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
    public static <T> ArrayList<T> readEntityFromCsv(String filename,Class T){
        ArrayList<T> items = new ArrayList<>();
        try{
            File dir = new File("data");
            if (dir.mkdirs()) {
                System.out.println("Created data dir");
            }
            File f = new File(dir, filename + ".csv");
            if (!f.exists()) {
                System.out.println("file does not exist");
                f.createNewFile();
            }
            File file = new File("data/" + filename + ".csv");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = "";
            while ((line= bufferedReader.readLine())!=null){
                String[] data = line.split(",");
                if(T== DeviceEntity.class){
                    Device device = new Device();
                    device.setId(data[0]);
                    device.setName(data[1]);
                    device.setPublishFrequency(Integer.parseInt(data[2]));
                    device.setMessageSize(Integer.parseInt(data[3]));
                    device.setDataDistribution(data[4]);
                    String[] topics = data[5].split(";");
                    List<String> deviceTopics = new ArrayList<>();
                    for (String topic : topics) {
                        deviceTopics.add(topic);
                    }
                    device.setCapturesObservation(deviceTopics);
                    DeviceEntity deviceEntity = new DeviceEntity(Double.parseDouble(data[6]),Double.parseDouble(data[7]));
                    deviceEntity.setDevice(device);
                    items.add((T) deviceEntity);
                }
                else if (T== ApplicationEntity.class){
                    Application app = new Application();
                    app.setId(data[0]);
                    app.setName(data[1]);
                    app.setPriority(Integer.parseInt(data[2]));
                    app.setProcessingRate(Integer.parseInt(data[3]));
                    app.setApplicationCategory(data[4]);
                    String[] topics = data[5].split(";");
                    List<String> appTopics = new ArrayList<>();
                    for (String topic : topics) {
                        appTopics.add(topic);
                    }
                    app.setRecievesObservation(appTopics);
                    ApplicationEntity applicationEntity = new ApplicationEntity(Double.parseDouble(data[6]),Double.parseDouble(data[7]));
                    applicationEntity.setApplication(app);
                    items.add((T) applicationEntity);
                }  else{
                    System.err.println("Error: Class not found");
                }
            }

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

}
