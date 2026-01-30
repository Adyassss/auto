package api.models.comparison;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class ModelComparisonConfigLoader {

    public static class ComparisonRule {
        private final String responseClassName;
        private final List<String> fields;

        public ComparisonRule(String responseClassName, List<String> fields) {
            this.responseClassName = responseClassName;
            this.fields = fields;
        }

        public String getResponseClassName() {
            return responseClassName;
        }

        public List<String> getFields() {
            return fields;
        }
    }

    private final Map<String, ComparisonRule> rules = new HashMap<>();

    public ModelComparisonConfigLoader(String configFile) {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configFile)) {
            if (input == null) {
                throw new IllegalArgumentException("Config file not found: " + configFile);
            }

            Properties props = new Properties();
            props.load(input);

            for (String key : props.stringPropertyNames()) {
                String[] target = props.getProperty(key).split(":");
                if (target.length != 2) continue;

                String responseClassName = target[0].trim();
                String[] fieldParts = target[1].split(",");
                List<String> fields = new ArrayList<>(fieldParts.length);
                for (String part : fieldParts) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        fields.add(trimmed);
                    }
                }
                rules.put(key.trim(), new ComparisonRule(responseClassName, fields));
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to load DTO comparison config", e);
        }
    }

    public ComparisonRule getRuleFor(String requestClassName) {
        return rules.get(requestClassName);
    }
}
