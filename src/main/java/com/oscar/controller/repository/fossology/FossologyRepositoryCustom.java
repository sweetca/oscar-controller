package com.oscar.controller.repository.fossology;

import com.oscar.controller.model.fossology.FossologyScan;

public interface FossologyRepositoryCustom {

    FossologyScan findScanByComponent(String component);

    FossologyScan findScanByComponentAndVersion(String component, String version);
}
