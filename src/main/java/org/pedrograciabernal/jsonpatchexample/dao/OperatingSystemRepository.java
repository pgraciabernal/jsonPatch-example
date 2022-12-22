package org.pedrograciabernal.jsonpatchexample.dao;

import org.pedrograciabernal.jsonpatchexample.model.OperatingSystem;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OperatingSystemRepository {

    private List<OperatingSystem> operatingSystems;

    public OperatingSystem findOne(int id) {
        return operatingSystems.stream()
                .filter(p -> p.getAppOperatingSystemId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Operating system with id {} not found", id));
    }

    public OperatingSystem save(OperatingSystem operatingSystem) {

        operatingSystems.removeIf(os -> os.getAppOperatingSystemId() == operatingSystem.getAppOperatingSystemId());
        operatingSystems.add(operatingSystem);

        return findOne(operatingSystem.getAppOperatingSystemId());
    }

    public List<OperatingSystem> getOs() {
        return operatingSystems;
    }

    public void setOs(List<OperatingSystem> operatingSystems) {
        this.operatingSystems = operatingSystems;
    }
}
