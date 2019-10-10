package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.Sender;
import org.junit.Test;

public class SenderControllerRestAssuredIntegrationTests extends BaseTests<Sender> {

    public SenderControllerRestAssuredIntegrationTests() {
        super("http://localhost:8080/api/senders", Sender.class);
    }

    @Test
    public void checkGetAllSenders() {
        this.checkGetAll();
    }

    @Test
    public void  checkGetById_BAD(){
        this.checkGetById();
    }
}
