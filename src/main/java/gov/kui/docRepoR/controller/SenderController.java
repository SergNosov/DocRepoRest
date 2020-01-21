package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.service.SenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4201")
public class SenderController {
    private final SenderService senderService;

    @Autowired
    public SenderController(SenderService senderService) {
        this.senderService = senderService;
    }

    @GetMapping("/senders")
    public List<Sender> getAllSenders() {
        List<Sender> senderList = senderService.findAll();
        System.out.println(senderList);
        return senderList;
    }

    @GetMapping("/senderspage")
    public ResponseEntity<PagedResources<Sender>> getAllSenders(Pageable pageable,
                                                                PagedResourcesAssembler assembler,
                                                                Sort sort) {
        if (sort.isUnsorted()) {
            sort = Sort.by("id");
        }

        Pageable pagableSorting = PageRequest.of(0, pageable.getPageSize(), sort);
        Page<Sender> senders = senderService.findAllPage(pagableSorting);

        PagedResources<Sender> pr = assembler.toResource(senders,
                linkTo(SenderController.class).slash("/senderspage").withSelfRel());

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Link", createLinkHeader(pr));

        return new ResponseEntity<>(assembler.toResource(senders, linkTo(SenderController.class)
                .slash("/senderspage").withSelfRel()), responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/senders/{id}")
    public Sender getSender(@PathVariable int id) {
        return senderService.findById(id);
    }

    @PostMapping("/senders")
    public Sender addSender(@RequestBody @Valid Sender sender) {
        sender.setId(0);
        return senderService.save(sender);
    }

    @PutMapping("/senders")
    public Sender updateSender(@RequestBody @Valid Sender sender) {
        if (sender.getId() == 0) {
            throw new RuntimeException("Неверное значение sender.id." +
                    " Для обновления (update) значение не должно быть 0.");
        }
        Sender updatedSender = senderService.save(sender);
        System.out.println("updatedSender = " + updatedSender);
        return updatedSender;
    }

    @DeleteMapping("/senders/{id}")
    public CommonMessage deleteSender(@PathVariable int id) {
        int deletingId = senderService.deleteById(id);
        return new CommonMessage("Удален отправитель id - " + deletingId);
    }

    private String createLinkHeader(PagedResources<Sender> pr) {
        final StringBuilder linkHeader = new StringBuilder();
        linkHeader.append(buildLinkHeader(pr.getLinks("first").get(0).getHref(), "first"));
        linkHeader.append(", ");
        linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(), "next"));
        return linkHeader.toString();
    }

    public static String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}
