package gov.kui.docRepoR.controller;

import gov.kui.docRepoR.dto.DoctypeDto;
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
    public List<DoctypeDto> getAllDoctypes(){
        return doctypeService.findAll();
    }

    @GetMapping("/doctypes/{id}")
    public DoctypeDto getDoctype(@PathVariable int id){
        DoctypeDto doctypeDto = doctypeService.findById(id);
        return  doctypeDto;
    }
}
