package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.Entity.Doctype;
import gov.kui.docRepoR.service.DoctypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
