package gov.kui.docRepoR.model;

import java.time.LocalDateTime;

public class CommonMessage {

    private String message;
    private LocalDateTime timeStamp;

    public CommonMessage(){
        this.timeStamp = LocalDateTime.now();
    }

    public CommonMessage(String message) {
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
        return "CommonMessage{" +
                "message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
