package lenori.lenoriaddons.io;

import java.lang.String;
import java.lang.IllegalStateException;

import java.util.UUID;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class IgnoreListElement {
  private static final String NAME_ID = "name", UUID_ID = "uuid", TIMESTAMP_ID = "timestamp";
  private final String name;
  private final UUID id;
  private final long timestamp;

  public IgnoreListElement(String name, UUID id, long timeStamp) {
    this.name = name;
    this.id = id;
    this.timestamp = timeStamp;
  }

  public JsonElement save() {
    JsonObject object = new JsonObject();
    object.addProperty(NAME_ID, name);
    object.addProperty(UUID_ID, id.toString());
    object.addProperty(TIMESTAMP_ID, timestamp);
    return object;
  }

  public static IgnoreListElement deserialize(JsonElement element) {
    if (!element.isJsonObject()) throw new IllegalStateException("Element is not a json object.");
    JsonObject object = (JsonObject) element;
    return new IgnoreListElement(object.getAsJsonPrimitive(NAME_ID).getAsString(), UUID.fromString(object.getAsJsonPrimitive(UUID_ID).getAsString()), object.getAsJsonPrimitive(TIMESTAMP_ID).getAsLong());
  }
}
