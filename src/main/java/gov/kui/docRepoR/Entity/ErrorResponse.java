package gov.kui.docRepoR.Entity;

import java.time.LocalDateTime;

public class ErrorResponse {

    private String message;
    private LocalDateTime timeStamp;

    public ErrorResponse(String message, LocalDateTime timeStamp) {
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
