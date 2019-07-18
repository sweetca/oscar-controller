package com.oscar.controller.service;

import com.oscar.controller.dto.VulnerabilityRequestDto;
import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.component.ComponentNvd;
import com.oscar.controller.model.ort.OrtScan;
import com.oscar.controller.repository.component.ComponentNvdRepository;
import com.oscar.controller.repository.component.ComponentRepository;
import com.oscar.controller.repository.nvd.VulnerabilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@Slf4j
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final VulnerabilityRepository vulnerabilityRepository;
    private final ComponentNvdRepository componentNvdRepository;
    private final OrtScanService ortScanService;
    private final TaskService taskService;

    public ComponentService(ComponentRepository componentRepository,
                            VulnerabilityRepository vulnerabilityRepository,
                            ComponentNvdRepository componentNvdRepository,
                            OrtScanService ortScanService,
                            TaskService taskService) {
        this.componentRepository = componentRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
        this.componentNvdRepository = componentNvdRepository;
        this.ortScanService = ortScanService;
        this.taskService = taskService;
    }

    public List<Component> findAll() {
        return this.componentRepository.findAll(new Sort(Sort.Direction.DESC, "date"));
    }

    public Component findComponent(String id, String version) {
        Component component = this.componentRepository.findByIdAndVersion(id, version).orElseThrow(OscarDataException::noComponentFound);
        component.setTask(this.taskService.findFullTaskByComponentAndVersion(id, version).orElse(null));
        return component;
    }

    public ComponentNvd findComponentVulnerabilities(String component, String version) {
        return this.componentNvdRepository
                .findByComponentAndVersion(component, version)
                .orElseThrow(OscarDataException::noComponentData);
    }

    @Async
    public CompletableFuture<Boolean> proceedComponentVulnerabilities(String id, String version) {
        Map<String, Set<String>> nvdMap = new HashMap<>();
        OrtScan scan = this.ortScanService.readScanOpt(id, version).orElse(null);
        if (scan == null) {
            return CompletableFuture.completedFuture(false);
        }
        scan.getReport().getPackages().parallelStream().forEach(p -> {
            VulnerabilityRequestDto dto = new VulnerabilityRequestDto();

            String[] data = p.getName().split(":");
            dto.setVendor(data[1]);
            dto.setName(data[2]);

            if (isNotBlank(p.getHomepage())) {
                dto.setHomePage(p.getHomepage());
            }
            if (p.getVcs() != null && isNotBlank(p.getVcs().getUrl())) {
                dto.setUrl(p.getVcs().getUrl());
            }

            try {
                nvdMap.put(p.getName(), this.vulnerabilityRepository.matchIds(dto));
            } catch (Exception e) {
                log.error("Error findComponentVulnerabilities", e);
            }
        });

        ComponentNvd nvd = new ComponentNvd();
        nvd.setComponent(id);
        nvd.setVersion(version);
        nvd.setNvdMap(nvdMap);
        this.componentNvdRepository.save(nvd);

        return CompletableFuture.completedFuture(true);
    }
}
