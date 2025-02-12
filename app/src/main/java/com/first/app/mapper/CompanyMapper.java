package com.first.app.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import com.first.app.dto.CompanyDTO;
import com.first.app.entity.Company;

@Service
@Mapper(componentModel = "spring")
public interface CompanyMapper {
    
    Company toEntity(CompanyDTO dto);
    CompanyDTO toDto(Company entity);
}
