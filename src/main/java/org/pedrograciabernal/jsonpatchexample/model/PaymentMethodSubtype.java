package org.pedrograciabernal.jsonpatchexample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentMethodSubtype {
    private int paymentMethodSubtypeId;
    private String paymentMethodSubtypeName;
    private boolean active;
}
