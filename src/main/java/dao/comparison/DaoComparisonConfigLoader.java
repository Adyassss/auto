package dao.comparison;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DaoComparisonConfigLoader {

    private final Map<String, DaoComparisonRule> rules = new HashMap<>();

    public DaoComparisonConfigLoader(String configFile) {
        InputStream input = null;

        // Попытка через ClassLoader
        input = getClass().getClassLoader().getResourceAsStream(configFile);

        // Если не нашли, пробуем через getResource (с /)
        if (input == null) {
            input = getClass().getResourceAsStream("/" + configFile);
        }

        System.out.println(input != null ? "Файл найден: " + configFile : "Файл не найден: " + configFile);

        if (input == null) {
            throw new IllegalArgumentException("Config file not found: " + configFile);
        }

        try (InputStream inStream = input) {
            Properties props = new Properties();
            props.load(inStream);

            for (String key : props.stringPropertyNames()) {
                String[] target = props.getProperty(key).split(":");
                if (target.length != 2) continue;

                String daoClassName = target[0].trim();
                List<String> fields = Arrays.asList(target[1].split(","));

                rules.put(key.trim(), new DaoComparisonRule(daoClassName, fields));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load DAO comparison config", e);
        }
    }

    public DaoComparisonRule getRuleFor(Class<?> apiResponseClass) {
        return rules.get(apiResponseClass.getSimpleName());
    }

    public static class DaoComparisonRule {
        private final String daoClassSimpleName;
        private final Map<String, String> fieldMappings;

        public DaoComparisonRule(String daoClassSimpleName, List<String> fieldPairs) {
            this.daoClassSimpleName = daoClassSimpleName;
            this.fieldMappings = new HashMap<>();

            for (String pair : fieldPairs) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    fieldMappings.put(parts[0].trim(), parts[1].trim());
                } else {
                    fieldMappings.put(pair.trim(), pair.trim());
                }
            }
        }

        public String getDaoClassSimpleName() {
            return daoClassSimpleName;
        }

        public Map<String, String> getFieldMappings() {
            return fieldMappings;
        }
    }
}