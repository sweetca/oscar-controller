package com.oscar.controller.api;

import com.oscar.controller.model.ort.OrtScanError;
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

    @PostMapping("/report/{component}/{version}")
    public ResponseEntity<?> uploadStackScanResult(@PathVariable String component,
                                                   @PathVariable String version,
                                                   @RequestBody OrtScanReport report) {
        log.info("new scan result for scan {}/{}", component, version);
        report.setComponent(component);
        report.setVersion(version);
        report.setDate(new Date());
        return ResponseEntity.ok(this.ortScanService.saveReport(report).getId());
    }

    @PostMapping("/error/{component}/{version}")
    public ResponseEntity<?> uploadStackScanError(@PathVariable String component,
                                                  @PathVariable String version,
                                                  @RequestBody OrtScanError error) {
        log.info("new scan error for scan {}/{}", component, version);
        error.setComponent(component);
        error.setVersion(version);
        return ResponseEntity.ok(this.ortScanService.saveError(error).getId());
    }

    @PostMapping("/html/{component}/{version}")
    public ResponseEntity<?> uploadReport(@PathVariable String component,
                                          @PathVariable String version,
                                          @RequestBody String content) {
        log.info("new scan html for scan {}/{}", component, version);
        return ResponseEntity.ok(this.ortScanService.saveHtml(content, component, version).getId());
    }

    @PostMapping("/logs/{component}/{version}")
    public ResponseEntity<?> uploadLogs(@PathVariable String component,
                                        @PathVariable String version,
                                        @RequestBody String content) {
        log.info("new scan logs for scan {}/{}", component, version);
        return ResponseEntity.ok(this.ortScanService.saveLogs(content, component, version).getId());
    }
}
