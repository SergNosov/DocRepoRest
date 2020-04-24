package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Sender;
import gov.kui.docRepoR.dto.SenderDto;
import gov.kui.docRepoR.facade.SenderServiceFacade;
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
    private final SenderServiceFacade senderFacade;
    private final SenderService senderService;

    @Autowired
    public SenderController(SenderServiceFacade senderFacade, SenderService senderService) {
        this.senderFacade = senderFacade;
        this.senderService = senderService;
    }

    @GetMapping("/senders")
    public List<SenderDto> getAllSenders() {
        return senderFacade.findAll();
    }

    @GetMapping("/senders/{id}")
    public SenderDto getSender(@PathVariable int id) {
        return senderFacade.findById(id);
    }

    @PostMapping("/senders")
    public SenderDto addSender(@RequestBody @Valid SenderDto sender) {
        sender.setId(0);
        return senderFacade.save(sender);
    }

    @PutMapping("/senders")
    public SenderDto updateSender(@RequestBody @Valid SenderDto sender) {
        if (sender.getId() == 0) {
            throw new IllegalArgumentException("Неверное значение sender.id. " +
                    "При обновлении(update) id не должно быть равно 0.");
        }
        return senderFacade.update(sender);
    }

    @DeleteMapping("/senders/{id}")
    public CommonMessage deleteSender(@PathVariable int id) {
        int deletingId = senderFacade.deleteById(id);
        return new CommonMessage("Удален отправитель id - " + deletingId);
    }

    private String createLinkHeader(PagedResources<Sender> pr) {
        final StringBuilder linkHeader = new StringBuilder();
        linkHeader.append(buildLinkHeader(pr.getLinks("first").get(0).getHref(), "first"));
        linkHeader.append(", ");
        linkHeader.append(buildLinkHeader(pr.getLinks("next").get(0).getHref(), "next"));
        return linkHeader.toString();
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

    public static String buildLinkHeader(final String uri, final String rel) {
        return "<" + uri + ">; rel=\"" + rel + "\"";
    }
}
