package lenori.lenoriaddons;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

public class MojangAPIClient {
    private static URL url;

    public static String getUUID(String name, int timestamp) {
        try {
            if (timestamp >= 0) {
                url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name + "?at=" + timestamp);
            } else {
                url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            }

            InputStreamReader reader = new InputStreamReader(url.openStream());
            String uuid = new JsonParser().parse(reader).getAsJsonObject().get("id").getAsString();
            return uuid;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image getSkin(String uuid) {
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader = new InputStreamReader(url.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String textureJson = new String(Base64.getDecoder().decode(textureProperty.get("value").getAsString()));
            String signature = textureProperty.get("signature").getAsString();
            //textureProperty = new JsonParser().parse(textureJson).getAsJsonObject().get("textures").getAsJsonArray().get(0).getAsJsonObject();
            url = new URL(new JsonParser().parse(textureJson).getAsJsonObject().getAsJsonObject("textures").getAsJsonObject("SKIN").get("url").getAsString());
            Image image = null;
            image = ImageIO.read(url);
            return image;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }


    }

    public static String getName(String uuid) {
        try {
            URL url = new URL("https://api.mojang.com/user/profiles/" + uuid + "/names");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            String json = IOUtils.toString(url);
            JsonElement element = new JsonParser().parse(json);
            JsonArray nameArray = element.getAsJsonArray();
            JsonObject nameElement = nameArray.get(nameArray.size()-1).getAsJsonObject();
            return nameElement.get("name").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}