package se.kth.integral.omstoelk;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import se.kth.integral.oms.models.TableObject;
import se.kth.integral.oms.models.TableObjectColumnsItem;

public class TableObjectJsonAdapter implements JsonSerializer<TableObject> {
    public JsonElement serialize(TableObject src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonArray res = new JsonArray();

        for (List<String> row : src.rows()) {
            final List<TableObjectColumnsItem> columns = src.columns();
            final JsonObject json = new JsonObject();

            for (int i = 0; i < row.size(); i++) {
                switch (columns.get(i).type()) {
                case "bool":
                    json.addProperty(columns.get(i).name(), Boolean.valueOf(row.get(i)));
                    break;
                case "int":
                    json.addProperty(columns.get(i).name(), Integer.valueOf(row.get(i)));
                    break;
                case "long":
                    json.addProperty(columns.get(i).name(), Long.valueOf(row.get(i)));
                    break;
                case "real":
                    json.addProperty(columns.get(i).name(), Double.valueOf(row.get(i)));
                    break;
                default:
                    json.addProperty(columns.get(i).name(), row.get(i));
                }
            }
            res.add(json);
        }
        return res;
    }
}
