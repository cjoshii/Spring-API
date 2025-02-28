
package com.first.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.first.app.entity.Company;
import com.first.app.repository.CompanyRepository;
import com.first.app.repository.specification.CompanySpecification;
import com.first.app.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public Company CreateCompany(Company job) {
        return companyRepository.save(job);
    }

    @Override
    public Company GetById(long id) {
        return companyRepository.findById(id).orElse(null);
    }

    @Override
    public List<Company> GetAll() {
        return companyRepository.findAll();
    }

    @Override
    public List<Company> SearchCompanies(String name, String location, String description) {
        var spec = Specification.where(CompanySpecification.hasName(name))
                .and(CompanySpecification.hasDescription(description))
                .and(CompanySpecification.hasLocation(location));

        return companyRepository.findAll(spec);
    }

    @Override
    public Boolean DeleteCompany(long id) {
        companyRepository.deleteById(id);
        return true;
    }
}
