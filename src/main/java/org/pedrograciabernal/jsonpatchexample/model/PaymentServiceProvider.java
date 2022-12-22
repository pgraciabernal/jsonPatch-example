package org.pedrograciabernal.jsonpatchexample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaymentServiceProvider {
    private int paymentServiceProviderId;
    private String siteId;
    private String channel;
    private String paymentServiceProviderName;
    private boolean active;
    private int priority;
    private int weight;
    private List<ProviderRule> providerRules;
}
