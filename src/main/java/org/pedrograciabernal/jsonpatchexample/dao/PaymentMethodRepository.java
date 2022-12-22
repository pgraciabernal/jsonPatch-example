package org.pedrograciabernal.jsonpatchexample.dao;

import org.pedrograciabernal.jsonpatchexample.model.PaymentMethod;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentMethodRepository {

    private List<PaymentMethod> paymentMethods;

    public PaymentMethod findOne(int id) {
        return paymentMethods.stream()
                .filter(p -> p.getPaymentMethodId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payment method with id {} not found", id));
    }

    public PaymentMethod save(PaymentMethod paymentMethod) {

        paymentMethods.removeIf(pm -> pm.getPaymentMethodId() == paymentMethod.getPaymentMethodId());
        paymentMethods.add(paymentMethod);

        return findOne(paymentMethod.getPaymentMethodId());
    }

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }
}
