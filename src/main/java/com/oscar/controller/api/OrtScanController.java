package com.oscar.controller.api;

import com.oscar.controller.model.ort.OrtScanReport;
import com.oscar.controller.service.OrtScanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/ort")
@Slf4j
public class OrtScanController {

    private final OrtScanService ortScanService;

    @Autowired
    public OrtScanController(OrtScanService ortScanService) {
        this.ortScanService = ortScanService;
    }

    @GetMapping("/{component}")
    public ResponseEntity<?> readOrtScan(@PathVariable String component) {
        try {
            return ResponseEntity.ok(this.ortScanService.readScan(component));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{component}/{version}")
    public ResponseEntity<?> readOrtScan(@PathVariable String component,
                                         @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.ortScanService.readScan(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{component}/{version}/html")
    public ResponseEntity<?> readOrtScanHtml(@PathVariable String component,
                                             @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.ortScanService.readHtml(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{component}/{version}/logs")
    public ResponseEntity<?> readOrtScanLogs(@PathVariable String component,
                                             @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.ortScanService.readLogs(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/report/{component}/{version}")
    public ResponseEntity<?> uploadScanReport(@PathVariable String component,
                                              @PathVariable String version,
                                              @RequestBody OrtScanReport report) {
        log.info("save scan result for {}/{}", component, version);
        report.setComponent(component);
        report.setVersion(version);
        report.setDate(new Date());
        return ResponseEntity.ok(this.ortScanService.saveReport(report).getId());
    }

    @PostMapping("/error/{component}/{version}")
    public ResponseEntity<?> uploadScanError(@PathVariable String component,
                                             @PathVariable String version,
                                             @RequestBody String error) {
        log.info("save scan error for {}/{}", component, version);
        return ResponseEntity.ok(this.ortScanService.saveError(error, component, version).getId());
    }

    @PostMapping("/html/{component}/{version}")
    public ResponseEntity<?> uploadScanHtml(@PathVariable String component,
                                            @PathVariable String version,
                                            @RequestBody String content) {
        log.info("save scan html for {}/{}", component, version);
        return ResponseEntity.ok(this.ortScanService.saveHtml(content, component, version).getId());
    }

    @PostMapping("/logs/{component}/{version}")
    public ResponseEntity<?> uploadScanLogs(@PathVariable String component,
                                            @PathVariable String version,
                                            @RequestBody String content) {
        log.info("save scan logs for {}/{}", component, version);
        return ResponseEntity.ok(this.ortScanService.saveLogs(content, component, version).getId());
    }
}
