package com.oscar.controller.repository.ort;

import com.oscar.controller.model.ort.OrtScanReport;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrtScanReportRepository extends MongoRepository<OrtScanReport, String> {

    Optional<OrtScanReport> findByComponent(@Param("component") String component);

    Optional<OrtScanReport> findByComponentAndVersion(@Param("component") String component, @Param("version") String version);
}
