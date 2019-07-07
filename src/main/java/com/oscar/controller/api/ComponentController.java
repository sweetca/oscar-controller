package com.oscar.controller.api;

import com.oscar.controller.service.ComponentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/component")
public class ComponentController {

    private final ComponentService componentService;

    @Autowired
    public ComponentController(ComponentService componentService) {
        this.componentService = componentService;
    }

    @GetMapping(value = "")
    public ResponseEntity<?> findComponent() {
        try {
            return ResponseEntity.ok(this.componentService.findAll());
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{component}/{version}")
    public ResponseEntity<?> findComponent(@PathVariable String component,
                                      @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.componentService.findComponent(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{component}")
    public ResponseEntity<?> findComponent(@PathVariable String component) {
        try {
            return ResponseEntity.ok(this.componentService.findComponent(component));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }
}
