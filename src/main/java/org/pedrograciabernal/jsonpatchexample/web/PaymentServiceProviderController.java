package org.pedrograciabernal.jsonpatchexample.web;

import org.pedrograciabernal.jsonpatchexample.interfaces.JsonPatchInterface;
import org.pedrograciabernal.jsonpatchexample.model.PaymentServiceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.json.*;

@RestController
@RequestMapping("/payment-service-provider")
public class PaymentServiceProviderController {

    @Autowired
    JsonPatchInterface jsonPatchInterface;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PaymentServiceProvider> getPSP(@PathVariable Integer id) {
        return ResponseEntity.ok().body(
                jsonPatchInterface.getPaymentServiceProvider(id));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<PaymentServiceProvider> patchPSP(@PathVariable Integer id, @RequestBody JsonPatch patchDocument) {
        try {
            return jsonPatchInterface.executePatch(id, patchDocument);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
