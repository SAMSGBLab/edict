package dataParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

import guimodel.*;
import modelingEntities.ApplicationEntity;
import modelingEntities.DeviceEntity;


public class DataParser {
    public static void addToCsv(String filename, String model) {
        try {

            if (Exists(filename, model.split(",")[0])) {
                deleteFromCsv(filename, model.split(",")[0]);
            }
            FileWriter fileWriter = new FileWriter("data/" + filename + ".csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(model);
            bufferedWriter.newLine();
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static Boolean Exists(String filename, String entityId) {
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
            fileReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public static void deleteFromCsv(String filename, String modelId) {
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
            Files.delete(inputFile.toPath());
            Files.move(tempFile.toPath(), inputFile.toPath());

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
                if (T == Observation.class) {
                    items.add((T) readObservation(data));
                } else if (T == ApplicationCategory.class) {
                    items.add((T) readApplicationCategory(data));
                } else if (T ==Device.class){
                    items.add((T) readDevice(data));
                }else if (T == Application.class){
                    items.add((T) readApplication(data));
                } else {
                    System.err.println("Error: Class not found");
                }
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    public static <T> ArrayList<T> readEntityFromCsv(String filename, Class T) {
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
                if (T == DeviceEntity.class) {
                    DeviceEntity deviceEntity = new DeviceEntity(Double.parseDouble(data[6]), Double.parseDouble(data[7]), Double.parseDouble(data[8]), Double.parseDouble(data[9]));
                    deviceEntity.setDevice(readDevice(data));
                    items.add((T) deviceEntity);
                } else if (T == ApplicationEntity.class) {
                    ApplicationEntity applicationEntity = new ApplicationEntity(Double.parseDouble(data[6]), Double.parseDouble(data[7]), Double.parseDouble(data[8]), Double.parseDouble(data[9]));
                    applicationEntity.setApplication(readApplication(data));
                    items.add((T) applicationEntity);
                } else {
                    System.err.println("Error: Class not found");
                }
            }
            bufferedReader.close();
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }
    private static Device readDevice(String[] data){
        List<String> deviceTopics = new ArrayList<>();
        Collections.addAll(deviceTopics, data[5].split(";"));
        return new Device(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4], deviceTopics);
    }
    private static Application readApplication(String[] data){
        List<String> appTopics = new ArrayList<>();
        Collections.addAll(appTopics, data[5].split(";"));
        return new Application(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4], appTopics);
    }
    private static ApplicationCategory readApplicationCategory(String[] data){
        return new ApplicationCategory(data[0], data[1], data[2]);
    }
    private static Observation readObservation(String[] data) {
        Set<String> topicPublishers = new HashSet<>(Arrays.asList( data[2].split(";")));
        Set<String> topicSubscribers = new HashSet<>(Arrays.asList(data[3].split(";")));
        return new Observation(data[0], data[1], topicPublishers, topicSubscribers);
    }
}
