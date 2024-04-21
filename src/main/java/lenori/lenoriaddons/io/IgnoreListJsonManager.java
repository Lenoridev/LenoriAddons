package lenori.lenoriaddons.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import lenori.lenoriaddons.LenoriAddons;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IgnoreListJsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;
    public final Map<UUID, Long> ignoreData = new HashMap<>();

    public IgnoreListJsonManager(String filePath) {
        this.filePath = filePath;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        loadData();
    }

    public void addData(UUID uuid, Long timestamp) {
        ignoreList.putIfAbsent(uuid, timestamp);
        saveToFile();
    }

    private void saveToFile() {
        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(GSON.toJson(serialize()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement serialize() {
        JsonArray array = new JsonArray();
        ignoreData.forEach((uuid, timestamp) -> {
            JsonObject object = new JsonObject();
            object.addProperty("uuid", uuid.toString());
            object.addProperty("timestamp", timestamp);
            array.add(object);
        });
        return array;
    }

    public void loadData() {
        try {
            JsonElement element = Streams.parse(new JsonReader(new FileReader(filePath)));
            if (!element.isJsonArray()) {
                LenoriAddons.LOGGER.warn("IgnoreList is not a Json Array!");
                return;
            }
            JsonArray array = (JsonArray) element;
            for (JsonElement element : array) {
                if (!element.isJsonObject()) continue;
                JsonObject object = (JsonObject) element;
                UUID uuid = UUID.fromString(object.getAsJsonPrimitive("uuid").getAsString());
                long timestamp = oject.getAsJsonPrimitive("timestamp").getAsLong());
                ignoreData.put(uuid, timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
