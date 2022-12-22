package org.pedrograciabernal.jsonpatchexample.dao;

import org.pedrograciabernal.jsonpatchexample.model.ProviderRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProviderRulesRepository {

    private List<ProviderRule> providerRules;

    public ProviderRule findOne(int id) {
        return providerRules.stream()
                .filter(p -> p.getProviderRulesId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Provider Rule with id {} not found", id));
    }

    public ProviderRule save(ProviderRule providerRule) {

        providerRules.removeIf(pr -> pr.getProviderRulesId() == providerRule.getProviderRulesId());
        providerRules.add(providerRule);

        return findOne(providerRule.getProviderRulesId());
    }

    public List<ProviderRule> getPSP() {
        return providerRules;
    }

    public void setProviderRules(List<ProviderRule> providerRules) {
        this.providerRules = providerRules;
    }
}
