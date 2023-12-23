import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonBuilder {

  private Map<String, Object> jsonMap;

  private JsonBuilder() {
    this.jsonMap = new HashMap<>();
  }

  public static JsonBuilder create() {
    return new JsonBuilder();
  }

  public JsonBuilder addProperty(String key, Object value) {
    jsonMap.put(key, value);
    return this;
  }

  public JsonBuilder addNestedObject(String key, JsonBuilder nestedObject) {
    jsonMap.put(key, nestedObject.build());
    return this;
  }

  public JsonBuilder addArray(String key, Object[] array) {
    jsonMap.put(key, array);
    return this;
  }

  public String build() {
    return mapToString(jsonMap);
  }
  private static final String INDENTATION = "  "; // Two spaces for each level of indentation

  public static String mapToString(Map<String, Object> map) {
    return mapToString(map, 0);
  }

  private static String mapToString(Map<String, Object> map, int indentationLevel) {
    StringBuilder jsonString = new StringBuilder("{\n");

    Set<Map.Entry<String, Object>> entrySet = map.entrySet();
    for (Map.Entry<String, Object> entry : entrySet) {
      jsonString.append(getIndentation(indentationLevel))
              .append("\"").append(entry.getKey()).append("\": ");

      Object value = entry.getValue();
      if (value instanceof String) {
        jsonString.append("\"").append(value).append("\"");
      } else if (value instanceof Map) {
        jsonString.append(mapToString((Map<String, Object>) value, indentationLevel + 1));
      } else if (value instanceof Object[]) {
        jsonString.append(arrayToString((Object[]) value, indentationLevel + 1));
      } else {
        jsonString.append(value);
      }

      jsonString.append(",\n");
    }

    if (jsonString.length() > 2) {
      jsonString.deleteCharAt(jsonString.length() - 2); // Remove the trailing comma and newline
    }

    jsonString.append(getIndentation(indentationLevel)).append("}");

    return jsonString.toString();
  }

  private static String arrayToString(Object[] array, int indentationLevel) {
    StringBuilder arrayString = new StringBuilder("[\n");
    for (Object item : array) {
      arrayString.append(getIndentation(indentationLevel + 1));

      if (item instanceof String) {
        arrayString.append("\"").append(item).append("\"");
      } else {
        arrayString.append(item);
      }

      arrayString.append(",\n");
    }

    if (arrayString.length() > 2) {
      arrayString.deleteCharAt(arrayString.length() - 2); // Remove the trailing comma and newline
    }

    arrayString.append(getIndentation(indentationLevel)).append("]");

    return arrayString.toString();
  }

  private static String getIndentation(int indentationLevel) {
    StringBuilder indentation = new StringBuilder();
    for (int i = 0; i < indentationLevel; i++) {
      indentation.append(INDENTATION);
    }
    return indentation.toString();
  }

    public static void main(String[] args) {
    JsonBuilder jsonBuilder = JsonBuilder.create()
            .addProperty("name", "John Doe")
            .addProperty("age", 30)
            .addProperty("isStudent", false)
            .addNestedObject("address",
                    JsonBuilder.create()
                            .addProperty("city", "New York")
                            .addProperty("zipcode", "10001"))
            .addArray("languages", new String[]{"Java", "JavaScript", "Python"});

    String jsonString = jsonBuilder.build();
    System.out.println(jsonString);
  }
}
