package org.pedrograciabernal.jsonpatchexample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentMethodRule {
    private int paymentMethodRuleId;
    private boolean active;
    private Double minOrderValue;
    private Double maxOrderValue;
    private boolean combineWithGiftCard;
    private boolean express;
}
