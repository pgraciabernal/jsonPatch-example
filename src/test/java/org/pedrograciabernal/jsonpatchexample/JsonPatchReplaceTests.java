package org.pedrograciabernal.jsonpatchexample;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.json.JSONParser;
import org.pedrograciabernal.jsonpatchexample.dao.*;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pedrograciabernal.jsonpatchexample.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JsonPatchReplaceTests {

    private static final org.apache.logging.log4j.Logger log =
            org.apache.logging.log4j.LogManager.getLogger(JsonPatchReplaceTests.class);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentServiceProviderRepository paymentServiceProviderRepository;

    @Autowired
    private ProviderRulesRepository providerRulesRepository;

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PaymentMethodRuleRepository paymentMethodRuleRepository;

    @Autowired
    private PaymentMethodSubtypeRepository paymentMethodSubtypeRepository;

    @BeforeEach
    public void prepareData() {
        setupPaymentMethodSubtype();
        setupPaymentMethodRules();
        setupPaymentMethod();
        setupOperatingSystem();
        setupProviderRules();
        setupPaymentServiceProvider();
    }

    @AfterEach
    public void clearData() {
        paymentMethodSubtypeRepository.setPaymentMethodSubtype(null);
        paymentMethodRuleRepository.setPaymentMethodRules(null);
        paymentMethodRepository.setPaymentMethods(null);
        operatingSystemRepository.setOs(null);
        providerRulesRepository.setProviderRules(null);
        paymentServiceProviderRepository.setPSP(null);
    }

    @Test
    public void testReplaceProviderRules() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/providerRules/201/active\", " +
                "\"value\": \"false\" }]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.readTree(patchPayload).toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        log.info(response.toPrettyString());

        assertNotNull(response);
    }

    @Test
    public void testReplaceOperatingSystem() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/providerRules/201/operatingSystems/906/minSupportedMinorVersion\", " +
                "\"value\": \"10\" }]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.readTree(patchPayload).toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        log.info(response.toPrettyString());

        assertNotNull(response);
    }

    @Test
    public void testReplacePaymentProvider() {

        String patchPayload = "[ { \"op\": \"replace\", " +
                "\"path\": \"/active\", " +
                "\"value\": \"false\" }]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.readTree(patchPayload).toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        log.info(response.toPrettyString());

        assertNotNull(response);
    }

    @Test
    public void testReplaceMultipleProviderRules() {

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

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.readTree(patchPayload).toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        log.info(response.toPrettyString());

        assertNotNull(response);
    }

    @Test
    public void testReplaceMultiplePaymentMethods() {

        String patchPayload = "[{\"op\":\"replace\",\"path\":\"/paymentMethodsRules/501/subtypes/1014/active\",\"value\":false}," +
                "{\"op\":\"replace\",\"path\":\"/paymentMethodsRules/501/paymentMethodRules/604/maxOrderValue\",\"value\":10000}]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.readTree(patchPayload).toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        log.info(response.toPrettyString());

        assertNotNull(response);
    }

    @Test
    public void testReplaceMultipleMixed() {

        String patchPayload = "[{\"op\":\"replace\",\"path\":\"/paymentMethods/501/subtypes/1014/active\",\"value\":false}," +
                "{\"op\":\"replace\",\"path\":\"/paymentMethods/501/paymentMethodRules/604/maxOrderValue\",\"value\":10000}," +
                "{\"op\":\"replace\",\"path\":\"/providerRules/201/operatingSystems/906/minSupportedMinorVersion\",\"value\":10}," +
                "{\"op\":\"replace\",\"path\":\"/weight\",\"value\":75}]";

        ObjectMapper mapper = new ObjectMapper();
        try {
            log.info(mapper.readTree(patchPayload).toPrettyString());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        JsonNode responsePatch = restTemplate.patchForObject("/payment-service-provider/101", patchPayload, JsonNode.class);

        JsonNode response = restTemplate.getForObject("/payment-service-provider/101", JsonNode.class);

        log.info(response.toPrettyString());

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

    private void setupPaymentMethodSubtype() {
        List<PaymentMethodSubtype> paymentMethodSubtypeList = new ArrayList<>();

        PaymentMethodSubtype paymentMethodSubtype = new PaymentMethodSubtype();

        paymentMethodSubtype.setPaymentMethodSubtypeId(1014);
        paymentMethodSubtype.setPaymentMethodSubtypeName("VISA");
        paymentMethodSubtype.setActive(true);

        paymentMethodSubtypeList.add(paymentMethodSubtype);

        paymentMethodSubtype = new PaymentMethodSubtype();

        paymentMethodSubtype.setPaymentMethodSubtypeId(1016);
        paymentMethodSubtype.setPaymentMethodSubtypeName("AMEX");
        paymentMethodSubtype.setActive(false);

        paymentMethodSubtypeList.add(paymentMethodSubtype);

        paymentMethodSubtypeRepository.setPaymentMethodSubtype(paymentMethodSubtypeList);
    }

    private void setupPaymentMethodRules() {
        List<PaymentMethodRule> paymentMethodRuleList = new ArrayList<>();

        PaymentMethodRule paymentMethodRule = new PaymentMethodRule();

        paymentMethodRule.setPaymentMethodRuleId(604);
        paymentMethodRule.setMinOrderValue(0.01);
        paymentMethodRule.setMaxOrderValue(null);
        paymentMethodRule.setExpress(false);
        paymentMethodRule.setCombineWithGiftCard(true);
        paymentMethodRule.setActive(true);

        paymentMethodRuleList.add(paymentMethodRule);

        paymentMethodRuleRepository.setPaymentMethodRules(paymentMethodRuleList);
    }

    private void setupPaymentMethod() {
        List<PaymentMethod> paymentMethodList = new ArrayList<>();

        PaymentMethod paymentMethod = new PaymentMethod();

        paymentMethod.setPaymentMethodId(501);
        paymentMethod.setPaymentMethodName("CREDIT_CARD");
        paymentMethod.setActive(true);
        paymentMethod.setSubtypes(paymentMethodSubtypeRepository.getPaymentMethodSubtype());
        paymentMethod.setPaymentMethodRules(paymentMethodRuleRepository.getPaymentMethodRules());

        paymentMethodList.add(paymentMethod);

        paymentMethodRepository.setPaymentMethods(paymentMethodList);
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
        psp.setProviderRules(providerRulesRepository.getProviderRules());
        psp.setPaymentMethods(paymentMethodRepository.getPaymentMethods());

        paymentServiceProviderList.add(psp);

        paymentServiceProviderRepository.setPSP(paymentServiceProviderList);
    }
}
