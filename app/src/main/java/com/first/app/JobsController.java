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

import com.first.app.dto.JobDTO;
import com.first.app.entity.Job;
import com.first.app.mapper.JobMapper;
import com.first.app.service.JobService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/jobs")
@Tag(name = "Jobs", description = "Jobs management APIs")
public class JobsController {

    @Autowired 
    JobService jobService;

    @Autowired
    private JobMapper jobMapper;

    /**
     * @return List of all jobs
     */
    @GetMapping
    @Operation(summary = "Get all jobs", description = "Get all jobs")
    @ApiResponse(responseCode = "200", description = "List of all jobs")
    @ApiResponse(responseCode = "204", description = "No jobs found")
    public ResponseEntity<List<JobDTO>> GetAll() {
        var jobs = jobService.GetAll();
        if(jobs.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        
        var jobDtos = new ArrayList<JobDTO>();
        for (Job job : jobs) {
            jobDtos.add(jobMapper.toDto(job));
        }

        return ResponseEntity.ok().body(jobDtos);
    }

    /**
     * Get job by id
     * @param id
     * @return Job with the given id
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get job by id", description = "Get job by id")
    @ApiResponse(responseCode = "200", description = "Job with the given id")
    @ApiResponse(responseCode = "204", description = "No job found")
    @Parameters({
        @Parameter(name = "id", description = "Job id", required = true)
    })
    public ResponseEntity<JobDTO> GetById(@PathVariable int id){
        var job = jobService.GetById(id);
        if(job == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok().body(jobMapper.toDto(job));
    }

    /**
     * Add a new job
     * @param job
     * @return Response with the location of the new job
     */
    @PostMapping
    @Operation(summary = "Add job", description = "Add a new job")
    @ApiResponse(responseCode = "201", description = "Job created")
    public ResponseEntity<Void> AddJob(@RequestBody JobDTO job) {
        var jobEntity = jobMapper.toEntity(job);
        var newJob = jobService.CreateJob(jobEntity);
        URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(newJob.getId())
                        .toUri();
        return ResponseEntity.created(location).build();
    }

    /**
     * Search for jobs by title, location and description
     * @param title
     * @param location
     * @param description
     * @return List of jobs that match the search criteria
     */
    @GetMapping("/search")
    @Operation(summary = "Search jobs", description = "Search jobs by title, location and description")
    @ApiResponse(responseCode = "200", description = "List of jobs that match the search criteria")
    @ApiResponse(responseCode = "204", description = "No jobs found")
    @Parameters({
        @Parameter(name = "title", description = "Job title"),
        @Parameter(name = "location", description = "Job location"),
        @Parameter(name = "description", description = "Job description")
    })
    public ResponseEntity<List<JobDTO>> SearchJobs(@RequestParam(required = false) String title, 
                                                @RequestParam(required = false) String location, 
                                                @RequestParam(required = false) String description) {
        var jobs = jobService.SearchJobs(title, location, description);
        if(jobs.isEmpty()){
            return ResponseEntity.noContent().build();
        }

        var jobDtos = new ArrayList<JobDTO>();
        for (Job job : jobs) {
            jobDtos.add(jobMapper.toDto(job));
        }

        return  ResponseEntity.ok().body(jobDtos);   
    }

    /**
     * Delete job by id
     * @param id
     * @return Response entity with the status of the delete operation
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete job by id", description = "Delete job by id")
    @ApiResponse(responseCode = "200", description = "Job deleted")
    @ApiResponse(responseCode = "204", description = "No job found")
    @Parameters({
        @Parameter(name = "id", description = "Job id", required = true)
    })
    public ResponseEntity<Void> DeleteJob(@PathVariable int id) {
        var deleted = jobService.DeleteJob(id);
        if(!deleted){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().build();
    }
}
