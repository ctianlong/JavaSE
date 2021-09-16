package util.gson;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;

/**
 * @author ctl
 * @date 2021/8/10
 */
public class EmployeeDeserializer implements JsonDeserializer<Employee> {
	@Override
	public Employee deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		LocalDate localDate = LocalDate.of(jsonObject.get("year").getAsInt(),
				jsonObject.get("month").getAsInt(),
				jsonObject.get("day").getAsInt());
		Employee employee = new Employee(jsonObject.get("id").getAsInt(),
				jsonObject.get("firstName").getAsString(),
				jsonObject.get("active").getAsBoolean(),
				localDate);
		return employee;
	}
}
