package com.oscar.controller.repository.component;

import com.oscar.controller.model.component.Component;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ComponentRepository extends MongoRepository<Component, String> {

    Optional<Component> findByIdAndVersion(String id, String version);
}
