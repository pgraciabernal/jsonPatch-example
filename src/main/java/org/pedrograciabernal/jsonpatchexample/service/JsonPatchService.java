package org.pedrograciabernal.jsonpatchexample.service;

import org.pedrograciabernal.jsonpatchexample.dao.*;
import org.pedrograciabernal.jsonpatchexample.interfaces.JsonPatchInterface;
import org.pedrograciabernal.jsonpatchexample.model.*;
import org.pedrograciabernal.jsonpatchexample.web.JsonPatchMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.json.Json;
import javax.json.JsonPatch;
import javax.json.JsonPatchBuilder;
import java.util.*;
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
    private JsonPatchMapper<PaymentMethod> mapperPaymentMethod;

    @Autowired
    private JsonPatchMapper<PaymentMethodRule> mapperPaymentMethodRule;

    @Autowired
    private JsonPatchMapper<PaymentMethodSubtype> mapperPaymentMethodSubtype;


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

            PaymentServiceProvider paymentServiceProvider = paymentServiceProviderRepository.findOne(id);

            if (splitPatch.length == 1) {
                paymentServiceProvider = updatePSP(id, jsonPatch);
            } else if (splitPatch[0].equalsIgnoreCase("providerRules")) {
                ProviderRule providerRule = updateProviderRule(Integer.valueOf(splitPatch[1]), jsonPatch);
                paymentServiceProvider.setProviderRules(providerRulesRepository.getProviderRules());
            } else if (splitPatch[0].equalsIgnoreCase("operatingSystems")) {
                OperatingSystem operatingSystem = updateOperatingSystem(Integer.valueOf(splitPatch[1]), jsonPatch);
                List<ProviderRule> providerRules = paymentServiceProvider.getProviderRules();
                ProviderRule providerRule = providerRules.get(0);
                providerRule.setOperatingSystems(operatingSystemRepository.getOs());
                providerRulesRepository.save(providerRule);
                paymentServiceProvider.setProviderRules(providerRulesRepository.getProviderRules());
            } else if (splitPatch[0].equalsIgnoreCase("paymentMethods")) {
                PaymentMethod paymentMethod = updatePaymentMethod(Integer.valueOf(splitPatch[1]), jsonPatch);
                paymentServiceProvider.setPaymentMethods(paymentMethodRepository.getPaymentMethods());
            } else if (splitPatch[0].equalsIgnoreCase("paymentMethodRules")) {
                PaymentMethodRule paymentMethodRule = updatePaymentMethodRule(Integer.valueOf(splitPatch[1]), jsonPatch);
                List<PaymentMethod> paymentMethods = paymentServiceProvider.getPaymentMethods();
                PaymentMethod paymentMethod = paymentMethods.get(0);
                paymentMethod.setPaymentMethodRules(paymentMethodRuleRepository.getPaymentMethodRules());
                paymentMethodRepository.save(paymentMethod);
                paymentServiceProvider.setPaymentMethods(paymentMethodRepository.getPaymentMethods());
            } else if (splitPatch[0].equalsIgnoreCase("subtypes")) {
                PaymentMethodSubtype paymentMethodSubtype =
                        updatePaymentMethodSybtype(Integer.valueOf(splitPatch[1]), jsonPatch);
                List<PaymentMethod> paymentMethods = paymentServiceProvider.getPaymentMethods();
                PaymentMethod paymentMethod = paymentMethods.get(0);
                paymentMethod.setSubtypes(paymentMethodSubtypeRepository.getPaymentMethodSubtype());
                paymentMethodRepository.save(paymentMethod);
                paymentServiceProvider.setPaymentMethods(paymentMethodRepository.getPaymentMethods());
            }

            paymentServiceProviderRepository.save(paymentServiceProvider);

        });

        return ResponseEntity.noContent().build();
    }

    @Override
    public PaymentServiceProvider getPaymentServiceProvider(Integer id) {
        return paymentServiceProviderRepository.findOne(id);
    }

    private PaymentServiceProvider updatePSP(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        PaymentServiceProvider psp = paymentServiceProviderRepository.findOne(id);
        // Apply the patch
        PaymentServiceProvider pspPatched = mapperPaymentServiceProvider.apply(psp, patchDocument);
        return paymentServiceProviderRepository.save(pspPatched);
    }

    private ProviderRule updateProviderRule(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        ProviderRule pr = providerRulesRepository.findOne(id);
        // Apply the patch
        ProviderRule prPatched = mapperProviderRules.apply(pr, patchDocument);
        return providerRulesRepository.save(prPatched);
    }

    private OperatingSystem updateOperatingSystem(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        OperatingSystem os = operatingSystemRepository.findOne(id);
        // Apply the patch
        OperatingSystem osPatched = mapperOperatingSystem.apply(os, patchDocument);
        return operatingSystemRepository.save(osPatched);
    }

    private PaymentMethod updatePaymentMethod(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        PaymentMethod paymentMethod = paymentMethodRepository.findOne(id);
        // Apply the patch
        PaymentMethod paymentMethodPatched = mapperPaymentMethod.apply(paymentMethod, patchDocument);
        return paymentMethodRepository.save(paymentMethodPatched);
    }

    private PaymentMethodRule updatePaymentMethodRule(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        PaymentMethodRule paymentMethodRule = paymentMethodRuleRepository.findOne(id);
        // Apply the patch
        PaymentMethodRule paymentMethodRulePatched = mapperPaymentMethodRule.apply(paymentMethodRule, patchDocument);
        return paymentMethodRuleRepository.save(paymentMethodRulePatched);
    }

    private PaymentMethodSubtype updatePaymentMethodSybtype(Integer id, JsonPatch patchDocument) {
        // Find the model that will be patched
        PaymentMethodSubtype paymentMethodSubtype = paymentMethodSubtypeRepository.findOne(id);
        // Apply the patch
        PaymentMethodSubtype paymentMethodSubtypePatched = mapperPaymentMethodSubtype.apply(paymentMethodSubtype, patchDocument);
        return paymentMethodSubtypeRepository.save(paymentMethodSubtypePatched);
    }
}
