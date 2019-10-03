package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.service.DoctypeService;
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
public class DoctypeRestController {
    private DoctypeService doctypeService;

    @Autowired
    public DoctypeRestController(DoctypeService doctypeService) {
        this.doctypeService = doctypeService;
    }

    @GetMapping("/doctypes")
    public List<Doctype> getAllDoctypes(){
        return doctypeService.findAll();
    }

    @GetMapping("/doctypes/{id}")
    public Doctype getDoctype(@PathVariable int id){
        return  doctypeService.findById(id);
    }

    @PostMapping("/doctypes")
    public Doctype addDoctype(@RequestBody @Valid Doctype doctype){
        doctype.setId(0);
        return doctypeService.save(doctype);
    }

    @DeleteMapping("/doctypes/{id}")
    public String delDoctype(@PathVariable int id){
        doctypeService.deleteById(id);
        return "Удален отправитель id - "+id;
    }

    @PutMapping("/doctypes")
    public Doctype updateDoctype(@RequestBody @Valid Doctype doctype){
        if (doctype.getId() == 0){
            throw new RuntimeException("Illegal value of doctype.id. In update request id value must be not 0.");
        }
        return doctypeService.save(doctype);
    }
}
