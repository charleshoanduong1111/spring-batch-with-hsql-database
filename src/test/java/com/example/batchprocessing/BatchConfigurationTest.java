package com.example.batchprocessing;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

/**
 * Author: Charles Hoan Duong
 */
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class})
// This is to avoid clashing of several JobRepository instances using the same data source for several test classes
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBatchTest
@SpringBootTest // This is required to be able to used spring boot features such as profiles
class BatchConfigurationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;
    @Autowired
    private Job importUserJob;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(importUserJob);
    }

    @AfterEach
    void tearDown() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void importUserJob_WhenJobEnds_ThenStatusCompleted() throws Exception {
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        
        //Test Customer
        String sql = "SELECT * FROM CUSTOMER";

        List<Customer> customers = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Customer.class));
        
        for (Customer customer : customers) {
        	System.out.println("==> importUserJob_WhenJobEnds_ThenStatusCompleted >>Customer ID:" + customer.getId() );
        	System.out.println("==> importUserJob_WhenJobEnds_ThenStatusCompleted >>Customer Name:" + customer.getName());
        }
        
        //Test Person
        String sqlPerson = "SELECT * FROM people";

        List<PersonDTO> persons = jdbcTemplate.query(
        		sqlPerson,
                new BeanPropertyRowMapper(PersonDTO.class));
        
        for (PersonDTO person : persons) {
        	System.out.println("==> importUserJob_WhenJobEnds_ThenStatusCompleted >> Person ID:" + person.getPerson_ID() );
        	System.out.println("==> importUserJob_WhenJobEnds_ThenStatusCompleted >> Person Name:" + person.getName());
        }
    }
}
