package org.pedrograciabernal.jsonpatchexample;

import org.pedrograciabernal.jsonpatchexample.model.*;
import org.pedrograciabernal.jsonpatchexample.web.JsonPatchMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr353.JSR353Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JsonPatchExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonPatchExampleApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JSR353Module());
        return mapper;
    }

    @Bean
    public JsonPatchMapper<PaymentServiceProvider> pspJsonPatchMapper(ObjectMapper mapper) {
        return new JsonPatchMapper<>(mapper);
    }

    @Bean
    public JsonPatchMapper<ProviderRule> prJsonPatchMapper(ObjectMapper mapper) {
        return new JsonPatchMapper<>(mapper);
    }

    @Bean
    public JsonPatchMapper<OperatingSystem> osJsonPatchMapper(ObjectMapper mapper) {
        return new JsonPatchMapper<>(mapper);
    }

    @Bean
    public JsonPatchMapper<PaymentMethod> pmJsonPatchMapper(ObjectMapper mapper) {
        return new JsonPatchMapper<>(mapper);
    }

    @Bean
    public JsonPatchMapper<PaymentMethodRule> pmrJsonPatchMapper(ObjectMapper mapper) {
        return new JsonPatchMapper<>(mapper);
    }

    @Bean
    public JsonPatchMapper<PaymentMethodSubtype> pmsJsonPatchMapper(ObjectMapper mapper) {
        return new JsonPatchMapper<>(mapper);
    }
}
