package com.first.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.first.app.entity.Job;
import com.first.app.repository.JobRepository;
import com.first.app.repository.specification.JobSpecification;
import com.first.app.service.JobService;

@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jobRepository;

    @Override
    public Job CreateJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job GetById(long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public List<Job> GetAll() {
        return jobRepository.findAll();
    }

    @Override
    public List<Job> SearchJobs(String title, String location, String description) {

        Specification<Job> spec = Specification.where(JobSpecification.hasTitle(title))
                                                .and(JobSpecification.hasDescription(description))
                                                .and(JobSpecification.hasLocation(location));
        return jobRepository.findAll(spec);
    }
}
