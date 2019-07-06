package com.oscar.controller.service;

import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.ort.*;
import com.oscar.controller.repository.ort.OrtScanHtmlRepository;
import com.oscar.controller.repository.ort.OrtScanLogsRepository;
import com.oscar.controller.repository.ort.OrtScanReportRepository;
import com.oscar.controller.repository.ort.OrtScanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrtScanService {

    private final OrtScanRepository ortScanRepository;
    private final OrtScanReportRepository ortScanReportRepository;
    private final OrtScanLogsRepository ortScanLogsRepository;
    private final OrtScanHtmlRepository ortScanHtmlRepository;

    public OrtScanService(OrtScanRepository ortScanRepository,
                          OrtScanReportRepository ortScanReportRepository,
                          OrtScanLogsRepository ortScanLogsRepository,
                          OrtScanHtmlRepository ortScanHtmlRepository) {
        this.ortScanRepository = ortScanRepository;
        this.ortScanReportRepository = ortScanReportRepository;
        this.ortScanLogsRepository = ortScanLogsRepository;
        this.ortScanHtmlRepository = ortScanHtmlRepository;
    }

    public OrtScan readScan(String component) {
        OrtScan scan = this.ortScanRepository
                .findOrtScanByComponent(component)
                .orElseThrow(OscarDataException::noOrtScanFound);

        scan.setReport(this.ortScanReportRepository
                .findOrtScanByComponent(component).orElse(null));
        scan.setHtml(this.ortScanHtmlRepository
                .findOrtScanByComponent(component).orElse(null));
        scan.setLogs(this.ortScanLogsRepository
                .findOrtScanByComponent(component).orElse(null));

        return scan;
    }

    public OrtScan readScan(String component, String version) {
        OrtScan scan = this.ortScanRepository
                .findOrtScanByComponentAndVersion(component, version)
                .orElseThrow(OscarDataException::noOrtScanFound);

        scan.setReport(this.ortScanReportRepository
                .findOrtScanByComponentAndVersion(component, version).orElse(null));
        scan.setHtml(this.ortScanHtmlRepository
                .findOrtScanByComponentAndVersion(component, version).orElse(null));
        scan.setLogs(this.ortScanLogsRepository
                .findOrtScanByComponentAndVersion(component, version).orElse(null));

        return scan;
    }

    public OrtScan saveReport(final OrtScanReport report) {
        this.ortScanReportRepository.save(report);
        log.info("ort scan report created {}/{}", report.getComponent(), report.getVersion());
        return insureScan(report.getComponent(), report.getVersion());
    }

    public OrtScan saveError(final OrtScanError error) {
        OrtScan scan = insureScan(error.getComponent(), error.getVersion());
        scan.setError(error.getError());
        this.ortScanRepository.save(scan);
        log.info("ort scan error created {}/{}", error.getComponent(), error.getVersion());
        return scan;
    }

    public OrtScan saveHtml(final String html, final String component, final String version) {
        OrtScanHtml scanHtml = new OrtScanHtml();
        scanHtml.setComponent(component);
        scanHtml.setVersion(version);
        scanHtml.setContent(html);
        this.ortScanHtmlRepository.save(scanHtml);
        log.info("ort scan html created {}/{}", component, version);
        return insureScan(component, version);
    }

    public OrtScan saveLogs(final String logs, final String component, final String version) {
        OrtScanLogs scanLogs = new OrtScanLogs();
        scanLogs.setComponent(component);
        scanLogs.setVersion(version);
        scanLogs.setContent(logs);
        this.ortScanLogsRepository.save(scanLogs);
        log.info("ort scan logs created {}/{}", component, version);
        return insureScan(component, version);
    }

    private OrtScan insureScan(final String component, final String version) {
        return this.ortScanRepository.findOrtScanByComponentAndVersion(component, version).orElseGet(() -> {
            OrtScan scan = new OrtScan();
            scan.setComponent(component);
            scan.setVersion(version);
            scan = this.ortScanRepository.save(scan);
            log.info("ort scan created {}/{}", component, version);
            return scan;
        });
    }
}
