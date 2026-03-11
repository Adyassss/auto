package common.extensions;

import com.github.tomakehurst.wiremock.WireMockServer;
import common.annotations.FraudCheckMock;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.util.LinkedHashSet;
import java.util.Set;

import static com.github.tomakehurst.wiremock.client.WireMock.anyUrl;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public class FraudCheckWireMockExtension implements BeforeEachCallback, AfterEachCallback {
    private WireMockServer wireMockServer;

    @Override
    public void beforeEach(ExtensionContext context) {
        FraudCheckMock config = context.getTestMethod()
                .map(method -> method.getAnnotation(FraudCheckMock.class))
                .orElseGet(() -> context.getTestClass()
                        .map(clazz -> clazz.getAnnotation(FraudCheckMock.class))
                        .orElse(null));

        if (config == null) {
            return;
        }

        wireMockServer = new WireMockServer(wireMockConfig().port(config.port()));
        wireMockServer.start();
        configureFor("localhost", config.port());

        String responseBody = String.format("{\"transactionId\":\"mock-tx-1\",\"status\":\"%s\",\"message\":\"Mocked fraud check response\",\"fraudCheckStatus\":\"%s\",\"details\":\"%s\",\"decision\":\"%s\",\"riskScore\":%.1f,\"reason\":\"%s\",\"requiresManualReview\":%s,\"additionalVerificationRequired\":%s}",
                config.status(),
                config.decision(),
                config.reason(),
                config.decision(),
                config.riskScore(),
                config.reason(),
                config.requiresManualReview(),
                config.additionalVerificationRequired());

        Set<String> endpoints = new LinkedHashSet<>();
        endpoints.add(config.endpoint());
        endpoints.add("/fraud-check");
        endpoints.add("/fraud/check");
        endpoints.add("/api/v1/fraud-check");
        endpoints.add("/api/v1/fraud/check");
        endpoints.add("/check");

        for (String endpoint : endpoints) {
            stubFor(post(urlEqualTo(endpoint))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(responseBody)));

            stubFor(post(urlPathEqualTo(endpoint))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(responseBody)));
        }

        // Fallback: in case backend calls another fraud URL variant.
        stubFor(post(anyUrl())
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        if (wireMockServer != null && wireMockServer.isRunning()) {
            wireMockServer.stop();
        }
    }
}
