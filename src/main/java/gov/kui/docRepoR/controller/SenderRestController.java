package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.CommonMessage;
import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SenderRestController {
    private SenderService senderService;

    @Autowired
    public SenderRestController(SenderService senderService) {
        this.senderService = senderService;
    }

    @GetMapping("/senders")
    public List<Sender> getAllSenders(){
        return senderService.findAll();
    }

    @GetMapping("/senders/{id}")
    public Sender getSender(@PathVariable int id){
        return  senderService.findById(id);
    }

    @PostMapping("/senders")
    public Sender addSender(@RequestBody @Valid Sender sender) {
        sender.setId(0);
        return senderService.save(sender);
    }

    @PutMapping("/senders")
    public Sender updateSender(@RequestBody @Valid Sender sender) {
        if (sender.getId() == 0) {
            throw new RuntimeException("Illegal value of doctype.id. In update request id value must be not 0.");
        }
        return senderService.save(sender);
    }

    @DeleteMapping("/senders/{id}")
    public CommonMessage deleteSender(@PathVariable int id) {
        int deletingId = senderService.deleteById(id);
        return new CommonMessage("Удален отправитель id - " + deletingId);
    }
}
