package gov.kui.docRepoR.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ApiResponse<T> extends CommonMessage{
    private T result;

    public ApiResponse(int status, String statusText, String message, T result) {
        super(status, statusText, message);
        this.result = result;
    }
}
