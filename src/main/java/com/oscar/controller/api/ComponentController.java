package com.oscar.controller.api;

import com.oscar.controller.service.ComponentService;
import com.oscar.controller.service.FossologyService;
import com.oscar.controller.service.OrtScanService;
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
    private final FossologyService fossologyService;
    private final OrtScanService ortScanService;

    @Autowired
    public ComponentController(ComponentService componentService,
                               FossologyService fossologyService,
                               OrtScanService ortScanService) {
        this.componentService = componentService;
        this.fossologyService = fossologyService;
        this.ortScanService = ortScanService;
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

    @GetMapping(value = "/{component}/{version}/vulnerabilities")
    public ResponseEntity<?> findComponentVulnerabilities(@PathVariable String component,
                                                          @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.componentService.findComponentVulnerabilities(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{component}/{version}/fossology")
    public ResponseEntity<?> findScan(@PathVariable String component,
                                      @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.fossologyService.findScan(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{component}/{version}/ort")
    public ResponseEntity<?> readOrtScan(@PathVariable String component,
                                         @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.ortScanService.readScan(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{component}/{version}/ort/html")
    public ResponseEntity<?> readOrtScanHtml(@PathVariable String component,
                                             @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.ortScanService.readHtml(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{component}/{version}/ort/logs")
    public ResponseEntity<?> readOrtScanLogs(@PathVariable String component,
                                             @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.ortScanService.readLogs(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }
}
