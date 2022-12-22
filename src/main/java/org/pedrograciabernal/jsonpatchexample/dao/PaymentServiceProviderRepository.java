package org.pedrograciabernal.jsonpatchexample.dao;

import org.pedrograciabernal.jsonpatchexample.model.PaymentServiceProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentServiceProviderRepository {

    private List<PaymentServiceProvider> paymentServiceProviders;

    public PaymentServiceProvider findOne(int id) {
        return paymentServiceProviders.stream()
                .filter(p -> p.getPaymentServiceProviderId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("PSP with id {} not found", id));
    }

    public PaymentServiceProvider save(PaymentServiceProvider paymentServiceProvider) {

        paymentServiceProviders.removeIf(psp -> psp.getPaymentServiceProviderId() == paymentServiceProvider.getPaymentServiceProviderId());
        paymentServiceProviders.add(paymentServiceProvider);

        return findOne(paymentServiceProvider.getPaymentServiceProviderId());
    }

    public List<PaymentServiceProvider> getPSP() {
        return paymentServiceProviders;
    }

    public void setPSP(List<PaymentServiceProvider> paymentServiceProviders) {
        this.paymentServiceProviders = paymentServiceProviders;
    }
}
