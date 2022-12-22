package org.pedrograciabernal.jsonpatchexample.service;

import org.pedrograciabernal.jsonpatchexample.dao.OperatingSystemRepository;
import org.pedrograciabernal.jsonpatchexample.dao.PaymentServiceProviderRepository;
import org.pedrograciabernal.jsonpatchexample.dao.ProviderRulesRepository;
import org.pedrograciabernal.jsonpatchexample.interfaces.JsonPatchInterface;
import org.pedrograciabernal.jsonpatchexample.model.OperatingSystem;
import org.pedrograciabernal.jsonpatchexample.model.PaymentServiceProvider;
import org.pedrograciabernal.jsonpatchexample.model.ProviderRule;
import org.pedrograciabernal.jsonpatchexample.web.JsonPatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class JsonPatchService implements JsonPatchInterface {

    @Autowired
    private JsonPatchMapper<PaymentServiceProvider> mapperPaymentServiceProvider;

    @Autowired
    private JsonPatchMapper<ProviderRule> mapperProviderRules;

    @Autowired
    private JsonPatchMapper<OperatingSystem> mapperOperatingSystem;


    @Autowired
    private PaymentServiceProviderRepository paymentServiceProviderRepository;

    @Autowired
    private ProviderRulesRepository providerRulesRepository;

    @Autowired
    private OperatingSystemRepository operatingSystemRepository;
    private JsonPatch jsonPatch;

    @Override
    public ResponseEntity executePatch(Integer id, JsonPatch patchDocument)  throws Exception {
        Map patchMap = new HashMap<>();

        patchDocument.toJsonArray().stream().forEach(patch -> {
            patchMap.put(patch.asJsonObject().get("path").toString().replaceAll("\"", ""),
                    patch.asJsonObject().get("value").toString().replaceAll("\"", ""));
        });

        patchMap.forEach((k, v) -> {
            String lastItem = k.toString();
            if (StringUtils.countOccurrencesOf(k.toString(), "/") > 3) {
                lastItem = Stream.of(k.toString().split("(?<=\\G.*/.*/.*)/"))
                        .reduce((first, last) -> last).get();
            }

            if (lastItem.startsWith("/")) {
                lastItem = lastItem.substring(1, lastItem.length());
            }
            String [] splitPatch = lastItem.split("/");

            JsonPatchBuilder jsonPatchBuilder = Json.createPatchBuilder();
            if (v instanceof String) {
                jsonPatch = jsonPatchBuilder.replace("/"
                        .concat((splitPatch.length == 1)? splitPatch[0] : splitPatch[2]), v.toString()).build();
            } else if (v instanceof Integer) {
                jsonPatch = jsonPatchBuilder.replace("/"
                        .concat((splitPatch.length == 1)? splitPatch[0] : splitPatch[2]), v.toString()).build();
            } else if (v instanceof Boolean) {
                jsonPatch = jsonPatchBuilder.replace("/"
                        .concat((splitPatch.length == 1)? splitPatch[0] : splitPatch[2]), v.toString()).build();
            }

            if (splitPatch.length == 1) {
                updatePSP(id, jsonPatch);
            } else if (splitPatch[0].equalsIgnoreCase("providerRules")) {
                updateProviderRule(Integer.valueOf(splitPatch[1]), jsonPatch);
            } else if (splitPatch[0].equalsIgnoreCase("operatingSystems")) {
                updateOperatingSystem(Integer.valueOf(splitPatch[1]), jsonPatch);
            }
        });

        return ResponseEntity.noContent().build();
    }

    @Override
    public PaymentServiceProvider getPaymentServiceProvider(Integer id) {
        return paymentServiceProviderRepository.findOne(id);
    }

    private void updatePSP(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        PaymentServiceProvider psp = paymentServiceProviderRepository.findOne(id);
        // Apply the patch
        PaymentServiceProvider pspPatched = mapperPaymentServiceProvider.apply(psp, patchDocument);
        paymentServiceProviderRepository.save(pspPatched);
    }

    private void updateProviderRule(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        ProviderRule pr = providerRulesRepository.findOne(id);
        // Apply the patch
        ProviderRule prPatched = mapperProviderRules.apply(pr, patchDocument);
        providerRulesRepository.save(prPatched);
    }

    private void updateOperatingSystem(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        OperatingSystem os = operatingSystemRepository.findOne(id);
        // Apply the patch
        OperatingSystem osPatched = mapperOperatingSystem.apply(os, patchDocument);
        operatingSystemRepository.save(osPatched);
    }
}
