package com.example.batchprocessing;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.JdbcTransactionManager;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class BatchConfiguration {

	// tag::readerwriterprocessor[]
	
  
    
//    /*
//     * HSQL Database: This works fine
//     */
    @Bean
    public DataSource dataSource() {
    	System.out.println("==> HSQl datasource");
        EmbeddedDatabase embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .build();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(embeddedDatabase);
        jdbcTemplate.execute("create table CUSTOMER (id int primary key, name varchar(20));");
        for (int i = 1; i <= 10; i++) {
            jdbcTemplate.execute(String.format("insert into CUSTOMER values (%s, 'foo%s');", i, i));
        }
        
        String sql = "SELECT * FROM CUSTOMER";

        List<Customer> customers = jdbcTemplate.query(
                sql,
                new BeanPropertyRowMapper(Customer.class));
        
        for (Customer customer : customers) {
        	System.out.println("==> customer ID:" + customer.getId() );
        	System.out.println("==> customer Name:" + customer.getName());
        }
        
        return embeddedDatabase;
    }
    

//	@Bean
//	public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//		return new JdbcTemplate(dataSource);
//	}
    
    
	
	@Bean
	public FlatFileItemReader<Person> reader() {
		return new FlatFileItemReaderBuilder<Person>().name("personItemReader")
				.resource(new ClassPathResource("person-data.csv")).linesToSkip(1).delimited()
				.names("person_ID", "name", "firstName", "lastName", "middle", "email", "phone", "fax", "title")
				.targetType(Person.class).build();
	}

	@Bean
	public PersonItemProcessor processor() {
		return new PersonItemProcessor();
	}


	@Bean
	public JdbcBatchItemWriter<Person> writer(DataSource dataSource) {
		return new JdbcBatchItemWriterBuilder<Person>()
			.sql("INSERT INTO people (person_id, name, first_name, last_name, middle, email, phone, fax, title )"
					+ " VALUES (:person_ID, :name, :firstName, :lastName, :middle, :email, :phone, :fax, :title)")
			.dataSource(dataSource)
			.beanMapped()
			.build();
	}
	// end::readerwriterprocessor[]

	// tag::jobstep[]
	@Bean
	public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
		return new JobBuilder("importUserJob", jobRepository).listener(listener).start(step1).build();
	}

	@Bean
	public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,
			FlatFileItemReader<Person> reader, PersonItemProcessor processor, JdbcBatchItemWriter<Person> writer) {
		return new StepBuilder("step1", jobRepository).<Person, Person>chunk(3, transactionManager).reader(reader)
				.processor(processor).writer(writer).build();
	}
	// end::jobstep[]
}
