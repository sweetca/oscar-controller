package com.oscar.controller.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.fossology.FossologyScan;
import com.oscar.controller.model.fossology.dto.NodeParser;
import com.oscar.controller.model.fossology.dto.ScanResultDto;
import com.oscar.controller.repository.fossology.FossologyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;

import static org.apache.logging.log4j.util.Strings.isNotBlank;

@Service
@Slf4j
public class FossologyService {

    private final FossologyRepository fossologyRepository;

    private final ObjectMapper mapper;

    @Autowired
    public FossologyService(FossologyRepository fossologyRepository,
                            ObjectMapper mapper) {
        this.fossologyRepository = fossologyRepository;
        this.mapper = mapper;
    }

    public String persistScanResult(String dtoJson, String component, String version) {
        FossologyScan scan = this.insureScan(component, version);
        scan.setDate(new Date());
        scan.setOriginalResponse(dtoJson);

        try {
            ScanResultDto dto = this.scanResultFromJson(dtoJson);
            if (isNotBlank(dto.getError())) {
                scan.setError(dto.getError());
            } else {
                scan.setParsedResponse(new NodeParser(dto).result());
            }
        } catch (Exception e) {
            log.error("Fail parse node from scan result", e);
            scan.setError(e.getMessage());
        }
        scan = this.fossologyRepository.save(scan);
        return scan.getId();
    }

    public String persistScanError(String error, String component, String version) {
        FossologyScan scan = this.insureScan(component, version);
        scan.setDate(new Date());
        scan.setError(error);
        scan = this.fossologyRepository.save(scan);
        return scan.getId();
    }

    public FossologyScan findScan(String component, String version) {
        return this.fossologyRepository
                .findByComponentAndVersion(component, version)
                .orElseThrow(OscarDataException::noFossologyFound);
    }

    private FossologyScan insureScan(String component, String version) {
        return this.fossologyRepository
                .findByComponentAndVersion(component, version)
                .orElseGet(() -> {
                    FossologyScan dto = new FossologyScan();
                    dto.setComponent(component);
                    dto.setVersion(version);
                    return dto;
                });
    }

    private ScanResultDto scanResultFromJson(String json) throws IOException {
        return this.mapper.readValue(json, ScanResultDto.class);
    }
}
