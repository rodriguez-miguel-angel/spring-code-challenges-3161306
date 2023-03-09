package com.cecilireid.springchallenges.config;

import com.cecilireid.springchallenges.models.CateringJob;
import com.cecilireid.springchallenges.repositories.CateringJobRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * See <https://www.baeldung.com/introduction-to-spring-batch>.
 */

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    public CateringJobRepository repository;

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Bean
    @StepScope
    public FlatFileItemReader<CateringJob> reader() {
        FlatFileItemReader<CateringJob> reader = new FlatFileItemReader<>();
        reader.setResource(new ClassPathResource("upload.csv"));
        reader.setLinesToSkip(1);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        String[] tokens = { "id", "customerName", "phoneNumber", "email", "menu", "noOfGuests", "status" };
        tokenizer.setNames(tokens);
        DefaultLineMapper<CateringJob> lineMapper = new DefaultLineMapper<>();
        lineMapper.setFieldSetMapper(new CateringJobMapper());
        lineMapper.setLineTokenizer(tokenizer);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public RepositoryItemWriter<CateringJob> writer() {
        RepositoryItemWriter<CateringJob> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        return writer;
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step").<CateringJob, CateringJob>chunk(10)
                .reader(reader()).writer(writer()).build();
    }

    @Bean
    public Job uploadCateringJob() {
        return jobBuilderFactory.get("uploadCateringJob").start(step()).build();
    }

}
