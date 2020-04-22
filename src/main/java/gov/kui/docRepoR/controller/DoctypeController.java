package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.domain.Doctype;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.facade.DoctypeServiceFacade;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4201")
public class DoctypeController {
    private final DoctypeService doctypeService;
    private final DoctypeServiceFacade doctypeFacade;

    @Autowired
    public DoctypeController(DoctypeService doctypeService, DoctypeServiceFacade doctypeFacade) {
        this.doctypeService = doctypeService;
        this.doctypeFacade = doctypeFacade;
    }

    @GetMapping("/doctypes")
    public List<DoctypeDto> getAllDoctypes() {
        return doctypeFacade.findAll();
    }

    @GetMapping("/doctypes/{id}")
    public DoctypeDto getDoctype(@PathVariable int id) {
        return doctypeFacade.findById(id);
    }

    @PostMapping("/doctypesDto")
    public DoctypeDto addDoctypeDto(@RequestBody @Valid DoctypeDto doctype) {
        doctype.setId(0);
        return doctypeFacade.save(doctype);
    }

    @PostMapping("/doctypes")
    public Doctype addDoctype(@RequestBody @Valid Doctype doctype) {
        doctype.setId(0);
        return doctypeService.save(doctype);
    }

    @PutMapping("/doctypes")
    public Doctype updateDoctype(@RequestBody @Valid Doctype doctype) {
        if (doctype.getId() == 0) {
            throw new IllegalArgumentException("Неверное значение doctype.id. " +
                    "При обновлении(update) id не должно быть равно 0.");
        }
        return doctypeService.save(doctype);
    }

    @DeleteMapping("/doctypes/{id}")
    public CommonMessage deleteDoctype(@PathVariable int id) {
        int deletingId = doctypeService.deleteById(id);
        return new CommonMessage("Удален тип документа id - " + deletingId);
    }
}
