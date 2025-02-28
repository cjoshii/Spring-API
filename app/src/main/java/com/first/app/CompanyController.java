package com.first.app;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/companies")
@Tag(name = "Companies", description = "Companies management APIs")
public class CompanyController {

    @Autowired 
    CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    /**
     * @return List of all companies
     */
    @GetMapping
    @Operation(summary = "Get all companies", description = "Get all companies")
    @ApiResponse(responseCode = "200", description = "List of all companies")
    @ApiResponse(responseCode = "204", description = "No companies found")
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
    @Operation(summary = "Get company by id", description = "Get company by id")
    @ApiResponse(responseCode = "200", description = "Company with the given id")
    @ApiResponse(responseCode = "204", description = "No company found")
    @Parameters({
        @Parameter(name = "id", description = "Company id", required = true),
    })
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
    @Operation(summary = "Add a new company", description = "Add a new company")
    @ApiResponse(responseCode = "201", description = "Company created")
    public ResponseEntity<Void> AddJob(@RequestBody CompanyDTO company) {
        var companyEntity = companyMapper.toEntity(company);
        var newCompany = companyService.CreateCompany(companyEntity);
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
    @Operation(summary = "Search for companies", description = "Search for companies by name, location and description")
    @ApiResponse(responseCode = "200", description = "List of companies that match the search criteria")
    @ApiResponse(responseCode = "204", description = "No companies found")
    @Parameters({
        @Parameter(name = "name", description = "Company name", required = false),
        @Parameter(name = "location", description = "Company location", required = false),
        @Parameter(name = "description", description = "Company description", required = false),
    })
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

    /**
     * Delete company by id
     * @param id
     * @return Response entity with the status of the delete operation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete company by id", description = "Delete company by id")
    @ApiResponse(responseCode = "200", description = "Company deleted")
    @Parameters({
        @Parameter(name = "id", description = "Company id", required = true),
    })
    public ResponseEntity<Void> DeleteCompany(@PathVariable int id){
        var isDeleted = companyService.DeleteCompany(id);
        if(isDeleted){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
