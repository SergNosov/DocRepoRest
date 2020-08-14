package gov.kui.docRepoR.config;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class DocRepoErrorAtributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {

        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        errorAttributes.put("statusText",errorAttributes.get("status")+"; "+errorAttributes.get("error"));
        errorAttributes.put("message",errorAttributes.get("message")+"; "+errorAttributes.get("path"));
        errorAttributes.remove("error");
        errorAttributes.remove("path");

        return errorAttributes;
    }
}
