package dataParser;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import guimodel.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
public class NGSIConverter {
	

   static Map<String, Class> map = new HashMap<String, Class>();

    public static void generateNGSIfromCsv(String OutputFolder) {
        map.put("applicationCategories",ApplicationCategory.class);
        map.put("applications",Application.class);
        map.put("devices",Device.class);
        map.put("observations",Observation.class);
        for(String model : map.keySet()) {
            File file = new File(OutputFolder+"/"+model);
            if (!file.exists()) {
                if (file.mkdir()) {
                    System.out.println("Directory is created!");
                } else {
                 System.out.println("Failed to create directory!");
                 }
             }
            
        }
        File[] folders = new File(OutputFolder).listFiles();
        for(String model : map.keySet()) {
            ArrayList<Object> list=DataParser.readModelFromCSv(model,map.get(model));
            for (Object t : list) {
               if (t instanceof Device) {
                   NGSI_parse(new File(OutputFolder+"/"+model+"/"+((Device)t).getId().replace(":", "_")+".jsonld"), ((Device) t).toMap());
                   }
               else if (t instanceof Application)
                   NGSI_parse(new File(OutputFolder+"/"+model+"/"+((Application)t).getId().replace(":", "_")+".jsonld"), ((Application) t).toMap());
               else if (t instanceof ApplicationCategory)
                   NGSI_parse(new File(OutputFolder+"/"+model+"/"+((ApplicationCategory)t).getId().replace(":", "_")+".jsonld"), ((ApplicationCategory) t).toMap());
               else if (t instanceof Observation)
                   NGSI_parse(new File(OutputFolder+"/"+model+"/"+((Observation)t).getId().replace(":", "_")+".jsonld"), ((Observation) t).toMap());
                               
            }
        }
    }
    private static void NGSI_parse(File file, Map model) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(Include.NON_NULL);
        try {
			mapper.writeValue(file, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    } 
    
}
