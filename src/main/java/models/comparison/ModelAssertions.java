package models.comparison;

import org.assertj.core.api.AbstractAssert;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelAssertions extends AbstractAssert<ModelAssertions, Object> {

    private final Object request;
    private final Object response;

    private ModelAssertions(Object request, Object response) {
        super(request, ModelAssertions.class);
        this.request = request;
        this.response = response;
    }

    public static ModelAssertions assertThatModels(Object request, Object response) {
        return new ModelAssertions(request, response);
    }

    public ModelAssertions match() {
        ModelComparisonConfigLoader configLoader = new ModelComparisonConfigLoader("model-comparison.properties");
        ModelComparisonConfigLoader.ComparisonRule rule = configLoader.getRuleFor(request.getClass().getSimpleName());

        if (rule != null) {
            // Сравнение по правилам из конфига (модель -> модель)
            Map<String, String> fieldMappings = new HashMap<>();
            for (String f : rule.getFields()) {
                String trimmed = f.trim();
                int eq = trimmed.indexOf('=');
                if (eq > 0 && eq < trimmed.length() - 1) {
                    fieldMappings.put(trimmed.substring(0, eq).trim(), trimmed.substring(eq + 1).trim());
                }
            }

            ModelComparator.ComparisonResult result = ModelComparator.compareFields(request, response, fieldMappings);

            if (result.hasMismatches()) {
                throw new AssertionError("Model mismatch: " + result.getMismatches());
            }
        } else {
            // Нет правила в конфиге — сравниваем как простые значения (например, два String)
            if (!Objects.equals(request, response)) {
                throw new AssertionError("Values do not match: expected [" + request + "] but was [" + response + "]");
            }
        }

        return this;
    }
}
