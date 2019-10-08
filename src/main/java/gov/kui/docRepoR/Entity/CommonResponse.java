package gov.kui.docRepoR.Entity;

import java.time.LocalDateTime;

public class CommonResponse {

    private String message;
    private LocalDateTime timeStamp;

    public CommonResponse(){}

    public CommonResponse(String message) {
        this.message = message;
        this.timeStamp = LocalDateTime.now();
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
        return "CommonResponse{" +
                "message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
