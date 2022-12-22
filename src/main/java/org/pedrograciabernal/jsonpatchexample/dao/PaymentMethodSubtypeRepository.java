package org.pedrograciabernal.jsonpatchexample.dao;

import org.pedrograciabernal.jsonpatchexample.model.PaymentMethodSubtype;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentMethodSubtypeRepository {

    private List<PaymentMethodSubtype> paymentMethodSubtypes;

    public PaymentMethodSubtype findOne(int id) {
        return paymentMethodSubtypes.stream()
                .filter(p -> p.getPaymentMethodSubtypeId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payment method subtype with id {} not found", id));
    }

    public PaymentMethodSubtype save(PaymentMethodSubtype paymentMethodSubtype) {

        paymentMethodSubtypes.removeIf(pm -> pm.getPaymentMethodSubtypeId() == paymentMethodSubtype.getPaymentMethodSubtypeId());
        paymentMethodSubtypes.add(paymentMethodSubtype);

        return findOne(paymentMethodSubtype.getPaymentMethodSubtypeId());
    }

    public List<PaymentMethodSubtype> getPaymentMethodSubtype() {
        return paymentMethodSubtypes;
    }

    public void setPaymentMethodSubtype(List<PaymentMethodSubtype> paymentMethodsSubtypes) {
        this.paymentMethodSubtypes = paymentMethodsSubtypes;
    }
}
