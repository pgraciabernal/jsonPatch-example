package org.pedrograciabernal.jsonpatchexample.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProviderRule {
    private int providerRulesId;
    private boolean active;
    private List<OperatingSystem> operatingSystems;
}
