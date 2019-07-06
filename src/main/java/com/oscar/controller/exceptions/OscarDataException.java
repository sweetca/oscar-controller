package com.oscar.controller.exceptions;

public class OscarDataException extends IllegalStateException {

    private static final String NO_TASK_FOUND = "Task not found.";

    private static final String NO_JOB_FOUND = "Job not found.";

    private static final String NO_FOSSOLOGY_SCAN_FOUND = "Fossology scan not found.";

    private static final String NO_ORT_SCAN_FOUND = "Ort scan not found.";

    public OscarDataException(String message) {
        super(message);
    }

    public static OscarDataException noTaskFound() {
        return new OscarDataException(NO_TASK_FOUND);
    }

    public static OscarDataException noJobFound() {
        return new OscarDataException(NO_JOB_FOUND);
    }

    public static OscarDataException noFossologyFound() {
        return new OscarDataException(NO_FOSSOLOGY_SCAN_FOUND);
    }

    public static OscarDataException noOrtScanFound() {
        return new OscarDataException(NO_ORT_SCAN_FOUND);
    }
}
