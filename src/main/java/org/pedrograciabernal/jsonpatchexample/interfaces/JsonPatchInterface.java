package org.pedrograciabernal.jsonpatchexample.interfaces;

import org.pedrograciabernal.jsonpatchexample.model.PaymentServiceProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.json.JsonPatch;

public interface JsonPatchInterface {

    public ResponseEntity executePatch(Integer id, JsonPatch patchDocument) throws Exception;

    public PaymentServiceProvider getPaymentServiceProvider(Integer id);

}
