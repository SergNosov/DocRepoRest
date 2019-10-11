package gov.kui.docRepoR;

import gov.kui.docRepoR.Entity.Sender;
import org.apache.commons.lang3.RandomStringUtils;
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
    public void  checkGetSenderById(){
        Sender newSender = this.checkAddNewEntity(this.createRandomSender());
        this.checkEntityById_OK(newSender.getId());
        this.checkGetEntityById_BAD();
    }

    @Test
    public void checkAddNewSender(){
        Sender newSender = this.createRandomSender();
        this.checkAddNewEntity(newSender);
    }

    @Test
    public void checkDelSenderById(){
        Sender newSender = this.checkAddNewEntity(this.createRandomSender());
        this.checkDelEntityById(newSender.getId());
    }

    private Sender createRandomSender() {
        String senderTitle = RandomStringUtils.randomAlphanumeric(7);
        return new Sender(senderTitle);
    }
}
