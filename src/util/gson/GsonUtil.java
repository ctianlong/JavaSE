package util.gson;

import com.alibaba.fastjson.JSON;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ctl
 * @date 2021/1/16
 */
public class GsonUtil {

	public static Type MAP_TYPE = new TypeToken<Map<String, Object>>(){}.getType();
	public static Type MAP_STRING_TYPE = new TypeToken<Map<String, String>>(){}.getType();

	private static final Gson GSON;

	static {
		GSON = new GsonBuilder()
				.disableHtmlEscaping()
//				.registerTypeAdapter(BigDecimal.class, new JsonSerializer<BigDecimal>() {
//					@Override
//					public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
//						if (src == null) {
//							return null;
//						} else {
//							return new JsonPrimitive(src.toPlainString());
//						}
//					}
//				})
				// BigDecimal 序列化时避免显示科学计数
				.registerTypeAdapter(BigDecimal.class, new TypeAdapter<BigDecimal>() {
					@Override public BigDecimal read(JsonReader in) throws IOException {
						if (in.peek() == JsonToken.NULL) {
							in.nextNull();
							return null;
						}
						try {
							return new BigDecimal(in.nextString());
						} catch (NumberFormatException e) {
							throw new JsonSyntaxException(e);
						}
					}

					@Override public void write(JsonWriter out, BigDecimal value) throws IOException {
						if (value == null) {
							out.nullValue();
						} else {
							out.jsonValue(value.toPlainString());
						}
//						out.value(value);
					}
				})
				.create();
	}

	public static Gson gson() {
		return GSON;
	}

	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<>();
		map.put("url", "http://www.lofter.com/collectionManage.do?targetBlogName=qatest5");
		map.put("a", 2);
		map.put("b", 3.0);
		String content = gson().toJson(map);
		System.out.println(content);
		Map<String, Object> r1 = gson().fromJson(content, MAP_TYPE);
		System.out.println(r1);
//		System.out.println(gson().toJson(r1));
	}

	@Test
	public void test111() {
		// {"a":"9999999.99"}
//		String sss = "{\"a\":\"9999999.9999\"}";
		String sss = "{\"a\":\"0.0000001\"}";
		Sss sss1 = JSON.parseObject(sss, Sss.class);
//		System.out.println(sss1.getA().toPlainString());
		String jsontext = GSON.toJson(sss1);
		System.out.println(jsontext);
	}

	public static class Sss {

		private BigDecimal a;

		public BigDecimal getA() {
			return a;
		}

		public void setA(BigDecimal a) {
			this.a = a;
		}
	}


	@Test
	public void test2() {
		String json = "{\"data\":[{\"id\":1,\"quantity\":2,\"name\":\"apple\"}, {\"id\":3,\"quantity\":4,\"name\":\"orange\"}]}";
		System.out.println("json == " + json);
        Map<String, Object> map = new LinkedTreeMap<>();
        map = new Gson().fromJson(json, map.getClass());
        System.out.println(map);

//		GsonBuilder gsonBuilder = new GsonBuilder();
//		gsonBuilder.registerTypeAdapter(new TypeToken<Map <String, Object>>(){}.getType(),  new MapDeserializerDoubleAsIntFix());
//		Gson gson = gsonBuilder.create();
//		Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
//		System.out.println(map);
	}

	@Test
	public void testCustomSerializer() {
		Employee employee = new Employee(1, "tianlong", false, LocalDate.now());
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Boolean.class, new BooleanSerializer())
				.setPrettyPrinting()
				.create();
		String json = gson.toJson(employee);
		System.out.println(json);
	}

	@Test
	public void testCustomDeserializer() {
		String json = "{'id': 1001,"
				+ "'firstName': 'Lokesh',"
				+ "'active': true, "
				+ "'day': 11, "
				+ "'month': 8, "
				+ "'year': 2019}";
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(Employee.class, new EmployeeDeserializer())
				.create();
		Employee employee = gson.fromJson(json, Employee.class);
		System.out.println(employee);
	}

}
