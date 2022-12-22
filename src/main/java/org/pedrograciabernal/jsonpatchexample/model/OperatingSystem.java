package org.pedrograciabernal.jsonpatchexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OperatingSystem {
    private int appOperatingSystemId;
    private String appOperatingSystemName;
    private int minSupportedMajorVersion;
    private int minSupportedMinorVersion;
}
