package com.first.app.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Service;

import com.first.app.dto.JobDTO;
import com.first.app.entity.Job;

@Service
@Mapper(componentModel = "spring")
public interface JobMapper {
    
    Job toEntity(JobDTO jobDTO);
    JobDTO toDto(Job job);

}
