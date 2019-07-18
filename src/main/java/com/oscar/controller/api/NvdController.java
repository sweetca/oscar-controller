package com.oscar.controller.api;

import com.oscar.controller.repository.nvd.VulnerabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nvd")
public class NvdController {

    private final VulnerabilityRepository vulnerabilityRepository;

    @Autowired
    public NvdController(VulnerabilityRepository vulnerabilityRepository) {
        this.vulnerabilityRepository = vulnerabilityRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findComponentVulnerabilities(@PathVariable String id) {
        try {
            return ResponseEntity.ok(this.vulnerabilityRepository.findById(id).get());
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }
}
