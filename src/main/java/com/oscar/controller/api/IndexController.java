package com.oscar.controller.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class IndexController {

    @GetMapping(value = {"/", "/tine"})
    public ResponseEntity<?> get() {
        return ResponseEntity.ok(new Date().toString());
    }

    @PostMapping(value = {"/", "/tine"})
    public ResponseEntity<?> post() {
        return ResponseEntity.ok(new Date().toString());
    }

    @PutMapping(value = {"/", "/tine"})
    public ResponseEntity<?> put() {
        return ResponseEntity.ok(new Date().toString());
    }

    @DeleteMapping(value = {"/", "/tine"})
    public ResponseEntity<?> delete() {
        return ResponseEntity.ok(new Date().toString());
    }
}
