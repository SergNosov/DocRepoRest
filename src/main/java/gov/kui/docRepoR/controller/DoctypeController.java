package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.domain.CommonMessage;
import gov.kui.docRepoR.dto.DoctypeDto;
import gov.kui.docRepoR.facade.DoctypeServiceFacade;
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
public class DoctypeController {
    private final DoctypeServiceFacade doctypeFacade;

    @Autowired
    public DoctypeController(DoctypeServiceFacade doctypeFacade) {
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

    @PostMapping("/doctypes")
    public DoctypeDto addDoctypeDto(@RequestBody @Valid DoctypeDto doctype) {
        doctype.setId(0);
        return doctypeFacade.save(doctype);
    }

    @PutMapping("/doctypes")
    public DoctypeDto updateDoctype(@RequestBody @Valid DoctypeDto doctype) {
        if (doctype.getId() == 0) {
            throw new IllegalArgumentException("Неверное значение doctype.id. " +
                    "При обновлении(update) id не должно быть равно 0.");
        }
        return doctypeFacade.update(doctype);
    }

    @DeleteMapping("/doctypes/{id}")
    public CommonMessage deleteDoctype(@PathVariable int id) {
        int deletingId = doctypeFacade.deleteById(id);
        return new CommonMessage("Удален тип документа id - " + deletingId);
    }
}
