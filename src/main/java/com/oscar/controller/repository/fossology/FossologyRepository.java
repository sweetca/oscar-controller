package com.oscar.controller.repository.fossology;

import com.oscar.controller.model.fossology.FossologyScan;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FossologyRepository extends MongoRepository<FossologyScan, String>, FossologyRepositoryCustom {
}
