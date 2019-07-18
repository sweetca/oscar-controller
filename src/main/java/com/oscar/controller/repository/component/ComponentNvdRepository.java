package com.oscar.controller.repository.component;

import com.oscar.controller.model.component.ComponentNvd;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ComponentNvdRepository extends MongoRepository<ComponentNvd, String> {

    Optional<ComponentNvd> findByIdAndVersion(String id, String version);
}
