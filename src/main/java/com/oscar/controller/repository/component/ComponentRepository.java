package com.oscar.controller.repository.component;

import com.oscar.controller.model.component.Component;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ComponentRepository extends MongoRepository<Component, String>, ComponentRepositoryCustom {
}
