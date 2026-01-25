package task1.engine;

public class JsonUtil {

    // For assignment simplicity:
    // We store everything as String.
    // This is enough to demonstrate durability.

    public static String serialize(Object obj) {
        if (obj == null) return null;
        return obj.toString();
    }

    public static <T> T deserialize(String value, Class<T> type) {
        if (value == null) return null;

        if (type == String.class) {
            return (T) value;
        }

        if (type == Integer.class) {
            return (T) Integer.valueOf(value);
        }

        if (type == Long.class) {
            return (T) Long.valueOf(value);
        }

        // For assignment scope, this is enough
        throw new RuntimeException("Unsupported type: " + type);
    }
}
