package util.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * @author ctl
 * @date 2021/8/10
 */
public class BooleanSerializer implements JsonSerializer<Boolean> {
	@Override
	public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
		if (src) {
			return new JsonPrimitive(1);
		}
		return new JsonPrimitive(0);
	}
}
