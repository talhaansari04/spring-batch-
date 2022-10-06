package com.gps.config;

import com.gps.model.OecEntity;
import com.gps.oecbatch.OecItemProcessor;
import com.gps.repository.OecRepo;
import com.gps.util.JobConstant;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableBatchProcessing
public class JobConfig {

    @Autowired
    private OecRepo oecRepo;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JobConstant.OEC_JOB)
                .flow(step())
                .end().build();

    }

    @Bean
    public Step step() {
        return stepBuilderFactory
                .get(JobConstant.OEC_STEP).<OecEntity, OecEntity>chunk(5)
                .reader(fileItemReader())
                .processor(oecItemProcessor())
                .writer(oecRepositoryItemWriter())
                .build();

    }

    @Bean
    public OecItemProcessor oecItemProcessor() {
        return new OecItemProcessor();
    }

    @Bean
    public RepositoryItemWriter<OecEntity> oecRepositoryItemWriter() {
        RepositoryItemWriter<OecEntity> repositoryItemWriter=new RepositoryItemWriter<>();
        repositoryItemWriter.setRepository(oecRepo);
        repositoryItemWriter.setMethodName("save");
        return repositoryItemWriter;
    }
    @Bean
    public FlatFileItemReader<OecEntity> fileItemReader(){
        FlatFileItemReader<OecEntity> fileItemReader=new FlatFileItemReader<>();
        fileItemReader.setResource(new FileSystemResource("src/main/resources/oec.csv"));
        fileItemReader.setLineMapper(defaultLineMapper());
        return fileItemReader;
    }
    private LineMapper<OecEntity> defaultLineMapper(){
        DefaultLineMapper<OecEntity> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer=new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(Boolean.FALSE);
        tokenizer.setNames("id","name","address","mobile_number");
        BeanWrapperFieldSetMapper<OecEntity> beanWrapperFieldSetMapper=new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(OecEntity.class);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        return lineMapper;
    }
}
