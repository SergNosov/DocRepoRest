package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<SenderDto> getAllSenders(){
        return senderService.findAll();
    }

    @GetMapping("/senders/{id}")
    public SenderDto getSender(@PathVariable int id){
        SenderDto senderDto = senderService.findById(id);
        return  senderDto;
    }
}
