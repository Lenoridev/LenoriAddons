package net.lenoriaddons.io;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jline.internal.Nullable;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class MojangAPIClient {
    private static URL url;
    private static final JsonParser jsonParser = new JsonParser();

    public static UUID getUUID(String name, int timestamp) {
        try {
            if (timestamp >= 0) {
                url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + timestamp);
            } else {
                url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            }

            InputStreamReader reader = new InputStreamReader(url.openStream());
            String idString = jsonParser.parse(reader).getAsJsonObject().get("id").getAsString();
            return UUID.fromString(idString.substring(0, 8) + "-" + idString.substring(8, 12) + "-" + idString.substring(12, 16) + "-" + idString.substring(16, 20) + "-" + idString.substring(20));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image getSkin(UUID uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String textureJson = new String(Base64.getDecoder().decode(textureProperty.get("value").getAsString()));
            url = new URL(new JsonParser().parse(textureJson).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString());
            Image image;
            image = ImageIO.read(url);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    @Nullable
    public static String getName(UUID uuid) {
        try {
            HttpURLConnection connection = getGetConnection("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            int status = connection.getResponseCode();
            InputStreamReader streamReader;

            if (status > 299) {
                return null;
            } else {
                streamReader = new InputStreamReader(connection.getInputStream());
            }

            JsonParser parser = new JsonParser();
            JsonObject object = (JsonObject) parser.parse(streamReader);
            return object.get("name").toString().replaceAll("\"", "");
        } catch (IOException e) {
            return null;
        }
    }

    private static HttpURLConnection getGetConnection(String url) throws IOException {
        URL statusURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) statusURL.openConnection();
        connection.setRequestMethod("GET");
        return connection;
    }
}