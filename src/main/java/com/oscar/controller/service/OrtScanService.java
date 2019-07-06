package com.oscar.controller.service;

import com.oscar.controller.exceptions.OscarDataException;
import com.oscar.controller.model.ort.*;
import com.oscar.controller.repository.ort.OrtScanHtmlRepository;
import com.oscar.controller.repository.ort.OrtScanLogsRepository;
import com.oscar.controller.repository.ort.OrtScanReportRepository;
import com.oscar.controller.repository.ort.OrtScanRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

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
                .findByComponent(component)
                .orElseThrow(OscarDataException::noOrtScanFound);

        scan.setReport(this.ortScanReportRepository
                .findByComponent(component).orElse(null));
        scan.setHtml(this.ortScanHtmlRepository
                .findByComponent(component).orElse(null));
        scan.setLogs(this.ortScanLogsRepository
                .findByComponent(component).orElse(null));

        return scan;
    }

    public OrtScan readScan(String component, String version) {
        OrtScan scan = this.ortScanRepository
                .findByComponentAndVersion(component, version)
                .orElseThrow(OscarDataException::noOrtScanFound);

        scan.setReport(this.ortScanReportRepository
                .findByComponentAndVersion(component, version).orElse(null));
        scan.setHtml(this.ortScanHtmlRepository
                .findByComponentAndVersion(component, version).orElse(null));
        scan.setLogs(this.ortScanLogsRepository
                .findByComponentAndVersion(component, version).orElse(null));

        return scan;
    }

    public OrtScan saveReport(final OrtScanReport report) {
        OrtScanReport storedReport = this.ortScanReportRepository
                .findByComponentAndVersion(report.getComponent(), report.getVersion())
                .orElseGet(() -> {
                    OrtScanReport dto = new OrtScanReport();
                    dto.setComponent(report.getComponent());
                    dto.setVersion(report.getVersion());
                    return dto;
                });
        storedReport.setDate(new Date());
        storedReport.setResult(report.getResult());
        storedReport.setType(report.getType());

        this.ortScanReportRepository.save(storedReport);
        log.info("ort scan report saved {}/{}", report.getComponent(), report.getVersion());
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
        OrtScanHtml scanHtml = this.ortScanHtmlRepository
                .findByComponentAndVersion(component, version)
                .orElseGet(OrtScanHtml::new);
        scanHtml.setComponent(component);
        scanHtml.setVersion(version);
        scanHtml.setContent(html);
        this.ortScanHtmlRepository.save(scanHtml);
        log.info("ort scan html created {}/{}", component, version);
        return insureScan(component, version);
    }

    public OrtScan saveLogs(final String logs, final String component, final String version) {
        OrtScanLogs scanLogs = this.ortScanLogsRepository
                .findByComponentAndVersion(component, version)
                .orElseGet(OrtScanLogs::new);
        scanLogs.setComponent(component);
        scanLogs.setVersion(version);
        scanLogs.setContent(logs);
        this.ortScanLogsRepository.save(scanLogs);
        log.info("ort scan logs created {}/{}", component, version);
        return insureScan(component, version);
    }

    private OrtScan insureScan(final String component, final String version) {
        return this.ortScanRepository.findByComponentAndVersion(component, version).orElseGet(() -> {
            OrtScan scan = new OrtScan();
            scan.setComponent(component);
            scan.setVersion(version);
            scan = this.ortScanRepository.save(scan);
            log.info("ort scan created {}/{}", component, version);
            return scan;
        });
    }
}
