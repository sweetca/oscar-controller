package com.oscar.controller.exceptions;

public class OscarDataException extends IllegalStateException {

    private static final String NO_TASK_FOUND = "Task not found.";

    private static final String NO_JOB_FOUND = "Job not found.";

    public OscarDataException(String message) {
        super(message);
    }

    public static OscarDataException noTaskFound() {
        return new OscarDataException(NO_TASK_FOUND);
    }

    public static OscarDataException noJobFound() {
        return new OscarDataException(NO_JOB_FOUND);
    }
}
