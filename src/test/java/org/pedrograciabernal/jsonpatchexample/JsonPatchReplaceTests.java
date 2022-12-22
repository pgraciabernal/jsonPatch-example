package org.pedrograciabernal.jsonpatchexample;

import org.pedrograciabernal.jsonpatchexample.dao.OperatingSystemRepository;
import org.pedrograciabernal.jsonpatchexample.dao.PaymentServiceProviderRepository;
import org.pedrograciabernal.jsonpatchexample.dao.ProviderRulesRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import org.pedrograciabernal.jsonpatchexample.model.OperatingSystem;
import org.pedrograciabernal.jsonpatchexample.model.PaymentServiceProvider;
import org.pedrograciabernal.jsonpatchexample.model.ProviderRule;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JsonPatchReplaceTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentServiceProviderRepository paymentServiceProviderRepository;

    @Autowired
    private ProviderRulesRepository providerRulesRepository;

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @BeforeEach
    public void prepareData() {
        setupOperatingSystem();
        setupProviderRules();
        setupPaymentServiceProvider();
    }

    @AfterEach
    public void clearData() {
        operatingSystemRepository.setOs(null);
        providerRulesRepository.setProviderRules(null);
        paymentServiceProviderRepository.setPSP(null);
    }

    @Test
    public void testReplaceProviderRules() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/providerRules/201/active\", " +
                "\"value\": \"false\" }]";

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        assertNotNull(response);
    }

    @Test
    public void testReplaceOperatingSystem() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/providerRules/201/operatingSystems/906/minSupportedMinorVersion\", " +
                "\"value\": \"10\" }]";

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        assertNotNull(response);
    }

    @Test
    public void testReplacePaymentProvider() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/active\", " +
                "\"value\": \"false\" }]";

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        assertNotNull(response);
    }

    @Test
    public void testReplaceMultiple() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/providerRules/201/operatingSystems/904/minSupportedMajorVersion\"," +
                "\"value\": 8" +
                "}," +
                "{\"op\": \"replace\"," +
                "\"path\": \"/providerRules/201/active\"," +
                "\"value\": false" +
                "}," +
                "{\"op\": \"replace\"," +
                "\"path\": \"/priority\"," +
                "\"value\": 2" +
                "}]";

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        assertNotNull(response);
    }

    @TestConfiguration
    static class TestRestTemplateJsonPatchConfiguration {

        private static final String APPLICATION_JSON_PATCH = "application/json-patch+json";

        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder()
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON_PATCH)
                    .requestFactory(HttpComponentsClientHttpRequestFactory::new);
        }
    }

    private void setupOperatingSystem() {
        List<OperatingSystem> operatingSystemList = new ArrayList<>();

        OperatingSystem os = new OperatingSystem();

        os.setAppOperatingSystemId(904);
        os.setAppOperatingSystemName("Android");
        os.setMinSupportedMajorVersion(7);
        os.setMinSupportedMinorVersion(10);

        operatingSystemList.add(os);

        os = new OperatingSystem();
        os.setAppOperatingSystemId(906);
        os.setAppOperatingSystemName("iOS");
        os.setMinSupportedMajorVersion(6);
        os.setMinSupportedMinorVersion(9);

        operatingSystemList.add(os);

        operatingSystemRepository.setOs(operatingSystemList);
    }

    private void setupProviderRules() {
        List<ProviderRule> providerRulesList = new ArrayList<>();

        ProviderRule pr = new ProviderRule();
        pr.setProviderRulesId(201);
        pr.setActive(true);
        pr.setOperatingSystems(operatingSystemRepository.getOs());

        providerRulesList.add(pr);

        providerRulesRepository.setProviderRules(providerRulesList);
    }

    private void setupPaymentServiceProvider() {
        List<PaymentServiceProvider> paymentServiceProviderList = new ArrayList<>();

        PaymentServiceProvider psp = new PaymentServiceProvider();
        psp.setPaymentServiceProviderId(101);
        psp.setActive(true);
        psp.setSiteId("adidas-CA");
        psp.setChannel("adidasConsumerApp");
        psp.setPaymentServiceProviderName("ACI");
        psp.setPriority(1);
        psp.setWeight(99);
        psp.setProviderRules(providerRulesRepository.getPSP());

        paymentServiceProviderList.add(psp);

        paymentServiceProviderRepository.setPSP(paymentServiceProviderList);
    }
}
