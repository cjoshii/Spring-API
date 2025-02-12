package com.first.app.service;

import java.util.List;

import com.first.app.entity.Company;

public interface CompanyService {
    
    public Company CreateCompany(Company job);
    
    public Company GetById(long id);
    
    public List<Company> GetAll();

    public List<Company> SearchCompanies(String name, String location, String description);
    
}
