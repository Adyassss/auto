package models.comparison;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ModelComparator {

    public static class ComparisonResult {
        private final List<Mismatch> mismatches;

        public ComparisonResult(List<Mismatch> mismatches) {
            this.mismatches = mismatches;
        }

        public boolean hasMismatches() {
            return !mismatches.isEmpty();
        }

        public List<Mismatch> getMismatches() {
            return mismatches;
        }
    }

    public static class Mismatch {
        private final String fieldName;

        public Mismatch(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }
    }

    public static <A, B> ComparisonResult compareFields(A request, B response, Map<String, String> fieldMappings) {
        List<Mismatch> mismatches = new ArrayList<>();
        for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
            String requestField = entry.getKey();
            String responseField = entry.getValue();

            Object value1 = getFieldValue(request, requestField);
            Object value2 = getFieldValue(response, responseField);

            if (!valuesEqual(value1, value2)) {
                mismatches.add(new Mismatch(requestField + " -> " + responseField));
            }
        }
        return new ComparisonResult(mismatches);
    }

    private static Object getFieldValue(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        while (clazz != null && clazz != Object.class) {
            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                return field.get(obj);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            } catch (Exception e) {
                throw new RuntimeException("Failed to get value for field: " + fieldName, e);
            }
        }
        throw new RuntimeException("Field not found: " + fieldName + " in " + obj.getClass().getSimpleName());
    }

    private static boolean valuesEqual(Object value1, Object value2) {
        if (Objects.equals(value1, value2)) {
            return true;
        }
        if (value1 instanceof Number && value2 instanceof Number) {
            return ((Number) value1).doubleValue() == ((Number) value2).doubleValue();
        }
        return Objects.equals(String.valueOf(value1), String.valueOf(value2));
    }
}
