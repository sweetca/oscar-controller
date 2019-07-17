package com.oscar.controller.service;

import com.oscar.controller.dto.VulnerabilityRequestDto;
import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.component.Component;
import com.oscar.controller.model.nvd.Vulnerability;
import com.oscar.controller.model.ort.OrtScan;
import com.oscar.controller.repository.component.ComponentRepository;
import com.oscar.controller.repository.fossology.FossologyRepository;
import com.oscar.controller.repository.nvd.VulnerabilityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Service
@Slf4j
public class ComponentService {

    private final ComponentRepository componentRepository;
    private final FossologyRepository fossologyRepository;
    private final VulnerabilityRepository vulnerabilityRepository;
    private final OrtScanService ortScanService;
    private final TaskService taskService;

    public ComponentService(ComponentRepository componentRepository,
                            FossologyRepository fossologyRepository,
                            VulnerabilityRepository vulnerabilityRepository,
                            OrtScanService ortScanService,
                            TaskService taskService) {
        this.componentRepository = componentRepository;
        this.fossologyRepository = fossologyRepository;
        this.vulnerabilityRepository = vulnerabilityRepository;
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

    public Map<String, Set<Vulnerability>> findComponentVulnerabilities(String id, String version) {
        Map<String, Set<Vulnerability>> vulnerabilities = new HashMap<>();
        OrtScan scan = this.ortScanService.readScanOpt(id, version).orElseThrow(OscarDataException::noOrtScanFound);
        scan.getReport().getPackages().forEach(p -> {
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

            vulnerabilities.put(p.getName(), this.vulnerabilityRepository.match(dto));

        });
        return vulnerabilities;
    }
}
