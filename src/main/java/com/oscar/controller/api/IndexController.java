package com.oscar.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class IndexController {

    @GetMapping(value = {"/", "/time"})
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(new Date().toString());
    }

    @PostMapping(value = {"/", "/time"})
    public ResponseEntity<?> post() {
        return ResponseEntity.ok(new Date().toString());
    }

    @PutMapping(value = {"/", "/time"})
    public ResponseEntity<?> put() {
        return ResponseEntity.ok(new Date().toString());
    }

    @DeleteMapping(value = {"/", "/time"})
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(new Date().toString());
    }
}
