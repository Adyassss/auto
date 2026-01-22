package models.comparison;

import org.assertj.core.api.AbstractAssert;
import java.util.HashMap;
import java.util.Map;

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
        // Загружаем конфиг
        ModelComparisonConfigLoader configLoader = new ModelComparisonConfigLoader("model-comparison.properties");
        ModelComparisonConfigLoader.ComparisonRule rule = configLoader.getRuleFor(request.getClass().getSimpleName());

        if (rule != null) {
            // Строим mapping: requestField -> responseField
            Map<String, String> fieldMappings = new HashMap<>();
            for (String f : rule.getFields()) {
                String[] parts = f.split("=");
                if (parts.length == 2) {
                    fieldMappings.put(parts[0].trim(), parts[1].trim());
                }
            }

            ModelComparator.ComparisonResult result = ModelComparator.compareFields(request, response, fieldMappings);

            if (result.hasMismatches()) {
                throw new AssertionError("Model mismatch: " + result.getMismatches());
            }
        }

        return this;
    }
}
