package lenori.lenoriaddons.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class IgnoreListJsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;
    private Map<Integer, Map<String, Object>> idToDataMap;

    public IgnoreListJsonManager(String filePath) {
        this.filePath = filePath;
        loadData();
    }
    // Method to add an ID and its corresponding name and timestamp to the map
    public void addData(int id, String name, String uuid, Long timestamp) {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", name);
        data.put("uuid", uuid);
        data.put("timestamp", timestamp);
        idToDataMap.put(id, data);
        saveToFile();
    }

    // Method to save the data to a JSON file
    private void saveToFile() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            GSON.toJson(idToDataMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to load data from the JSON file
    private void loadData() {
        FileReader reader = null;
        try {
            reader = new FileReader(filePath);
            Type typeOfMap = new TypeToken<Map<Integer, Map<String, Object>>>(){}.getType();
            idToDataMap = GSON.fromJson(reader, typeOfMap);
            if (idToDataMap == null) {
                idToDataMap = new HashMap<Integer, Map<String, Object>>();
            }
        } catch (IOException e) {
            e.printStackTrace();
            idToDataMap = new HashMap<Integer, Map<String, Object>>();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Method to get the UUID corresponding to a given id
    public String getUuidById(int id) {
        Map<String, Object> data = idToDataMap.get(id);
        if (data != null) {
            Object uuidObj = data.get("uuid");
            if (uuidObj instanceof String) {
                return uuidObj.toString();
            }
        }
        return null; //No UUID was found
    }

    // Method to get the name corresponding to a given id
    public String getNameById(int id) {
        Map<String, Object> data = idToDataMap.get(id);
        if (data != null) {
            return (String) data.get("name");
        } else {
            System.err.println("Failed to retrieve player name for id: " + id);
            return null;
        }
    }

    // Method to get the timestamp corresponding to a given id
    public long getTimestampById(int id) {
        Map<String, Object> data = idToDataMap.get(id);
        if (data != null) {
            Object timestampObj = data.get("timestamp");
            if (timestampObj instanceof Long) {
                return (Long) timestampObj;
            }
        } else {
            System.err.println("Failed to retrieve timestamp for id: " + id);
        }
        return -1; //No Timestamp was found
    }

}
