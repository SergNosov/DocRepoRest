package gov.kui.docRepoR.domain;

import java.time.LocalDateTime;

public class CommonMessage {

    private String message;
    private LocalDateTime timeStamp;

    public CommonMessage(){
        this.timeStamp = LocalDateTime.now();
    }

    public CommonMessage(String message) {
        this();
        this.message = message;
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
        return "CommonMessage{" +
                "message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
