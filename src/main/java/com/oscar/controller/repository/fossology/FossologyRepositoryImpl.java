package com.oscar.controller.repository.fossology;

import com.oscar.controller.model.fossology.FossologyScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class FossologyRepositoryImpl implements FossologyRepositoryCustom {

    private final MongoOperations mongoOperations;

    @Autowired
    public FossologyRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public FossologyScan findScanByComponent(String component) {
        Query query = new Query();
        query.addCriteria(Criteria.where("component").is(component));
        return this.mongoOperations.findOne(query, FossologyScan.class);
    }

    @Override
    public FossologyScan findScanByComponentAndVersion(String component, String version) {
        Query query = new Query();
        query.addCriteria(Criteria.where("component").is(component));
        query.addCriteria(Criteria.where("version").is(version));
        return this.mongoOperations.findOne(query, FossologyScan.class);
    }
}
