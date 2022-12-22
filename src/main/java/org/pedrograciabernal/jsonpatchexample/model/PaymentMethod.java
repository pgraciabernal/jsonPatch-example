package org.pedrograciabernal.jsonpatchexample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaymentMethod {
    private int paymentMethodId;
    private String paymentMethodName;
    private boolean active;
    private List<PaymentMethodRule> paymentMethodRules;
    private List<PaymentMethodSubtype> subtypes;
}
