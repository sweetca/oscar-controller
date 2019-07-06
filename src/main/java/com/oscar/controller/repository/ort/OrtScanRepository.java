package com.oscar.controller.repository.ort;

import com.oscar.controller.model.ort.OrtScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrtScanRepository extends MongoRepository<OrtScan, String> {

    Optional<OrtScan> findOrtScanByComponent(@Param("component") String component);

    Optional<OrtScan> findOrtScanByComponentAndVersion(@Param("component") String component, @Param("version") String version);
}
