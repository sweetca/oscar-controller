package com.oscar.controller.api;

import com.oscar.controller.dto.TaskRequestDto;
import com.oscar.controller.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping(value = "")
    public ResponseEntity<?> requestTask(@RequestBody TaskRequestDto taskRequest) {
        try {
            return ResponseEntity.ok(this.taskService.setupTask(taskRequest));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable String taskId) {
        try {
            return ResponseEntity.ok(this.taskService.findFullTaskById(taskId));
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/{taskId}/status")
    public ResponseEntity<?> getTaskStatus(@PathVariable String taskId) {
        try {
            boolean status = this.taskService.findStatusTaskById(taskId);
            return ResponseEntity.ok(status + "");
        } catch (Exception e) {
            return new ResponseEntity<>(new Error(e.getMessage(), e), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/running")
    public ResponseEntity<?> getRunningTasks() {
        return ResponseEntity.ok(this.taskService.findTaskStatusInProgress());
    }

    @GetMapping(value = "/latest")
    public ResponseEntity<?> getLatestTasks() {
        return ResponseEntity.ok(this.taskService.findLatestProgress());
    }
}
