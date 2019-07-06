package com.oscar.controller.repository.ort;

import com.oscar.controller.model.ort.OrtScanLogs;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrtScanLogsRepository extends MongoRepository<OrtScanLogs, String> {

    Optional<OrtScanLogs> findOrtScanByComponent(@Param("component") String component);

    Optional<OrtScanLogs> findOrtScanByComponentAndVersion(@Param("component") String component, @Param("version") String version);
}
