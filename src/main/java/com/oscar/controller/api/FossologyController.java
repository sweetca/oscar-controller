package com.oscar.controller.api;

import com.oscar.controller.service.FossologyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/fossology")
public class FossologyController {

    private final FossologyService fossologyService;

    @Autowired
    public FossologyController(FossologyService fossologyService) {
        this.fossologyService = fossologyService;
    }

    @PostMapping(value = "/{component}/{version}")
    public ResponseEntity<?> saveScanResult(@PathVariable String component,
                                            @PathVariable String version,
                                            @RequestBody String scan) {
        try {
            return ResponseEntity.ok(this.fossologyService.persistScanResult(scan, component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{component}/{version}")
    public ResponseEntity<?> findScan(@PathVariable String component,
                                      @PathVariable String version) {
        try {
            return ResponseEntity.ok(this.fossologyService.findScan(component, version));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{component}")
    public ResponseEntity<?> findScan(@PathVariable String component) {
        try {
            return ResponseEntity.ok(this.fossologyService.findScan(component));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }
}
