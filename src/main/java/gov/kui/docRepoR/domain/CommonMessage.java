package gov.kui.docRepoR.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "CommonMessage{" +
                "message='" + message + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }
}
