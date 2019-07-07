package com.oscar.controller.repository.fossology;

import com.oscar.controller.model.fossology.FossologyScan;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FossologyRepository extends MongoRepository<FossologyScan, String> {

    Optional<FossologyScan> findByComponent(@Param("component") String component);

    Optional<FossologyScan> findByComponentAndVersion(@Param("component") String component, @Param("version") String version);
}
