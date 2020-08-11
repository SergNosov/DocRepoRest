package gov.kui.docRepoR.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommonMessage {

    private int status;
    private String statusText;
    private String message;
    private LocalDateTime timeStamp;

    public CommonMessage(){
        this.timeStamp = LocalDateTime.now();
    }

    public CommonMessage(String message) {
        this();
        this.message = message;
    }

    public  CommonMessage(int status, String statusText, String message){
        this();
        this.status = status;
        this.statusText = statusText;
        this.message = message;
    }
}
