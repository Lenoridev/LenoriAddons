package net.lenoriaddons.io;

import com.google.gson.*;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import net.lenoriaddons.LenoriAddons;

import java.io.*;
import java.util.*;

public class IgnoreListJsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;
    public final Map<UUID, Long> ignoreList = new HashMap<>();

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
        ignoreList.forEach((uuid, timestamp) -> {
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
            for (JsonElement element1 : array) {
                if (!element1.isJsonObject()) {
                    LenoriAddons.LOGGER.warn("Element of JsonArray is not a JsonObject!");
                    continue;
                }
                JsonObject object = (JsonObject) element1;
                UUID uuid = UUID.fromString(object.getAsJsonPrimitive("uuid").getAsString());
                long timestamp = object.getAsJsonPrimitive("timestamp").getAsLong();
                ignoreList.put(uuid, timestamp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
