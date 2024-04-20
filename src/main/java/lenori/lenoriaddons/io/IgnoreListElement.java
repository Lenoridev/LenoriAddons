package lenori.lenoriaddons.io

import java.lang.String;
import java.lang.IllegalStateException;
import net.minecraft.util.GsonHelper;
import java.util.UUID;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class IgnoreListElement {
  private static final String NAME_ID = "Name", ID_ID = "Id", TIMESTAMP_ID = "TimeStamp";
  private final String name;
  private final UUID id;
  private final long timeStamp;

  public IgnoreListElement(String name, UUID id, long timeStamp) {
    this.name = name;
    this.id = id;
    this.timeStamp = timeStamp;
  }

  public JsonElement save() {
    JsonObject object = new JsonObject();
    object.addProperty(NAME_ID, name);
    object.addProperty(ID_ID, id.toString());
    object.addProperty(TIMESTAMP_ID, timeStamp);
    return object;
  }

  public static IgnoreListElement deserialize(JsonElement element) {
    if (!element.isJsonObject()) throw new IllegalStateException("detected element not being json object");
    JsonObject object = (JsonObject) element;
    return new IgnoreListElement(GsonHelper.getAsString(object, NAME_ID), UUID.fromString(GsonHelper.getAsString(object, ID_ID)), GsonHelper.getAsLong(object, TIMESTAMP_ID));
  }
}
