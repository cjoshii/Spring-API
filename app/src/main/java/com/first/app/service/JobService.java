package com.first.app.service;

import java.util.List;

import com.first.app.entity.Job;

public interface JobService {
    
    public Job CreateJob(Job job);
    
    public Job GetById(long id);
    
    public List<Job> GetAll();

    public List<Job> SearchJobs(String title, String location, String description);

    public Boolean DeleteJob(long id);
}
