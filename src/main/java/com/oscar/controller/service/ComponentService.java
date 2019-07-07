package com.oscar.controller.service;

import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;
import com.oscar.controller.repository.component.ComponentRepository;
import com.oscar.controller.repository.fossology.FossologyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final FossologyRepository fossologyRepository;
    private final OrtScanService ortScanService;
    private final TaskService taskService;

    public ComponentService(ComponentRepository componentRepository,
                            FossologyRepository fossologyRepository,
                            OrtScanService ortScanService,
                            TaskService taskService) {
        this.componentRepository = componentRepository;
        this.fossologyRepository = fossologyRepository;
        this.ortScanService = ortScanService;
        this.taskService = taskService;
    }

    public List<Component> findAll() {
        return this.componentRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
    }

    public Component findComponent(String id) {
        Component component = this.componentRepository.findById(id).orElseThrow(OscarDataException::noComponentFound);
        component.setFossologyScan(this.fossologyRepository.findByComponent(id).orElse(null));
        component.setOrtScan(this.ortScanService.readScanOpt(id).orElse(null));
        component.setTask(this.taskService.findFullTaskByComponent(id).orElse(null));
        return component;
    }

    public Component findComponent(String id, String version) {
        Component component = this.componentRepository.findByIdAndVersion(id, version).orElseThrow(OscarDataException::noComponentFound);
        component.setFossologyScan(this.fossologyRepository.findByComponentAndVersion(id, version).orElse(null));
        component.setOrtScan(this.ortScanService.readScanOpt(id, version).orElse(null));
        component.setTask(this.taskService.findFullTaskByComponentAndVersion(id, version).orElse(null));
        return component;
    }
}
