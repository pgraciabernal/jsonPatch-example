package org.pedrograciabernal.jsonpatchexample.dao;

import org.pedrograciabernal.jsonpatchexample.model.PaymentMethod;
import org.pedrograciabernal.jsonpatchexample.model.PaymentMethodRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PaymentMethodRuleRepository {

    private List<PaymentMethodRule> paymentMethodRules;

    public PaymentMethodRule findOne(int id) {
        return paymentMethodRules.stream()
                .filter(p -> p.getPaymentMethodRuleId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Payment method rule with id {} not found", id));
    }

    public PaymentMethodRule save(PaymentMethodRule paymentMethodRule) {

        paymentMethodRules.removeIf(pmr -> pmr.getPaymentMethodRuleId() == paymentMethodRule.getPaymentMethodRuleId());
        paymentMethodRules.add(paymentMethodRule);

        return findOne(paymentMethodRule.getPaymentMethodRuleId());
    }

    public List<PaymentMethodRule> getPaymentMethodRules() {
        return paymentMethodRules;
    }

    public void setPaymentMethodRules(List<PaymentMethodRule> paymentMethodRules) {
        this.paymentMethodRules = paymentMethodRules;
    }
}
