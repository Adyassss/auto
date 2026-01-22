package generators;

import com.github.curiousoddman.rgxgen.RgxGen;

import java.lang.reflect.*;
import java.util.*;

public class RandomModelGenerator {

    private static final Random random = new Random();

    public static  <T> T generate(Class<T> clazz) {
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();

            for (Field field : getAllFields(clazz)) {
                field.setAccessible(true);

                Object value;
                GeneratingRule rule = field.getAnnotation(GeneratingRule.class);

                if (rule != null) {
                    value = generateFromRegex(rule.regex(), field.getType());
                } else {
                    value = generateRandomValue(field);
                }

                field.set(instance, value);
            }

            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate model", e);
        }
    }

    // ---------- helpers ----------

    private static Object generateFromRegex(String regex, Class<?> type) {
        String result = new RgxGen(regex).generate();

        if (type == int.class || type == Integer.class) {
            return Integer.parseInt(result);
        }
        if (type == long.class || type == Long.class) {
            return Long.parseLong(result);
        }
        if (type == float.class || type == Float.class) {
            return Float.parseFloat(result);
        }
        if (type == double.class || type == Double.class) {
            return Double.parseDouble(result);
        }

        return result;
    }

    private static Object generateRandomValue(Field field) {
        Class<?> type = field.getType();

        if (type == String.class) {
            return UUID.randomUUID().toString().substring(0, 8);
        }
        if (type == int.class || type == Integer.class) {
            return random.nextInt(1000);
        }
        if (type == long.class || type == Long.class) {
            return random.nextLong();
        }
        if (type == float.class || type == Float.class) {
            return random.nextFloat() * 1000;
        }
        if (type == double.class || type == Double.class) {
            return random.nextDouble() * 1000;
        }
        if (type == boolean.class || type == Boolean.class) {
            return random.nextBoolean();
        }
        if (type.isEnum()) {
            Object[] values = type.getEnumConstants();
            return values[random.nextInt(values.length)];
        }
        if (List.class.isAssignableFrom(type)) {
            return generateRandomList(field);
        }

        return null;
    }

    private static List<String> generateRandomList(Field field) {
        Type genericType = field.getGenericType();

        if (genericType instanceof ParameterizedType pt) {
            Type actualType = pt.getActualTypeArguments()[0];
            if (actualType == String.class) {
                return List.of(
                        UUID.randomUUID().toString().substring(0, 5),
                        UUID.randomUUID().toString().substring(0, 5)
                );
            }
        }
        return Collections.emptyList();
    }

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        return fields;
    }
}
