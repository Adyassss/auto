package models.comparison;

import java.lang.reflect.Field;
import java.util.*;

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

            if (!Objects.equals(String.valueOf(value1), String.valueOf(value2))) {
                mismatches.add(new Mismatch(requestField + " -> " + responseField));
            }
        }
        return new ComparisonResult(mismatches);
    }

    private static Object getFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get value for field: " + fieldName, e);
        }
    }
}
