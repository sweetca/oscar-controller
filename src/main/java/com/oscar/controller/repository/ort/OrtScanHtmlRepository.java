package com.oscar.controller.repository.ort;

import com.oscar.controller.model.ort.OrtScanHtml;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrtScanHtmlRepository extends MongoRepository<OrtScanHtml, String> {

    Optional<OrtScanHtml> findOrtScanByComponent(@Param("component") String component);

    Optional<OrtScanHtml> findOrtScanByComponentAndVersion(@Param("component") String component, @Param("version") String version);
}
