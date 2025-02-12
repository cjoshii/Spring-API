package com.first.app.mapper;

import org.mapstruct.Mapper;

import com.first.app.dto.CompanyDTO;
import com.first.app.entity.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    
    Company toEntity(CompanyDTO dto);
    CompanyDTO toDto(Company entity);
}
