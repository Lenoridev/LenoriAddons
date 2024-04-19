package lenori.lenoriaddons.io;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.JsonReader;
import com.google.gson.reflect.TypeToken;

import lenori.lenoriaddons.io.IgnoreListElement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class IgnoreListJsonManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final String filePath;
    private final List<IgnoreListElement> ignoreList = new ArrayList<>(); 

    public IgnoreListJsonManager(String filePath) {
        this.filePath = filePath;
        loadData();
    }

    public void addData(String name, UUID uuid, Long timestamp) {
        ignoreList.add(new IgnoreListElement(name, uuid, timestamp));
        saveToFile();
    }

    private void saveToFile() {
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write(GSON.toJson(serialize()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JsonElement serialize() {
        JsonArray array = new JsonArray();
        ignoreList.stream().map(IgnoreListElement::save).forEach(array::add);
        return array;
    }

    public List<IgnoreListElement> loadData() {
        try {
            JsonElement element = Streams.parse(new JsonReader(new FileReader(filePath));
            if (!element.isJsonArray()) throw new IllegalStateException("detected config not being json array!");
            JsonArray array = (JsonArray) element;
            return new ArrayList<>(Stream.of(array).forEach(IgnoreListElement::deserialize).toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList();
        }
    }
}
