package com.first.app;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.first.app.dto.CompanyDTO;
import com.first.app.entity.Company;
import com.first.app.mapper.CompanyMapper;
import com.first.app.service.CompanyService;

@RestController
@RequestMapping("/companies")
public class CompanyController {

    @Autowired 
    CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * @return List of all companies
     */
    @GetMapping
    public ResponseEntity<List<CompanyDTO>> GetAll() {
        var companies = companyService.GetAll();
        if(companies.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        
        var dtos = new ArrayList<CompanyDTO>();
        for (Company company : companies) {
            dtos.add(companyMapper.toDto(company));
        }

        return ResponseEntity.ok().body(dtos);
    }

    /**
     * Get company by id
     * @param id
     * @return Company with the given id
     */
    @GetMapping("/{id}")
    public ResponseEntity<CompanyDTO> GetById(@PathVariable int id){
        var company = companyService.GetById(id);
        if(company == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(companyMapper.toDto(company));
    }

    /**
     * Add a new company
     * @param company
     * @return Response entity with the location of the new company
     */
    @PostMapping
    public ResponseEntity<Void> AddJob(@RequestBody Company company) {
        var newCompany = companyService.CreateCompany(company);
        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newCompany.getId())
                        .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Search for company by name, location and description
     * @param name
     * @param location
     * @param description
     * @return List of companies that match the search criteria
     */
    @GetMapping("/search")
    public ResponseEntity<List<CompanyDTO>> SearchJobs(@RequestParam(required = false) String name, 
                                                @RequestParam(required = false) String location, 
                                                @RequestParam(required = false) String description) {
        var companies = companyService.SearchCompanies(name, location, description);
        if(companies.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        var companyDtos = new ArrayList<CompanyDTO>();
        for (Company company : companies) {
            companyDtos.add(companyMapper.toDto(company));
        }

        return  ResponseEntity.ok(companyDtos);
    }
}
