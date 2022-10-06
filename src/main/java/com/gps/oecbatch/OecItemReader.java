package com.gps.oecbatch;

import com.gps.model.OecEntity;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import java.util.List;

public class OecItemReader implements ItemReader<OecEntity> {

    @Override
    public OecEntity read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        ExecutionContext exec = new ExecutionContext();

        FlatFileItemReader<OecEntity> fileItemReader = new FlatFileItemReader<>();
        fileItemReader.setResource(new FileSystemResource("src/main/resources/oec.csv"));
        fileItemReader.setLineMapper(defaultLineMapper());
        fileItemReader.open(exec);
        while (fileItemReader.read()!=null){
            OecEntity oec = fileItemReader.read();
            System.out.println(oec);
            return oec;
        }

        return null;
    }

    private LineMapper<OecEntity> defaultLineMapper() {
        DefaultLineMapper<OecEntity> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setStrict(Boolean.FALSE);
        tokenizer.setNames("id", "name", "address", "mobile_number");
        BeanWrapperFieldSetMapper<OecEntity> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setTargetType(OecEntity.class);
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);
        return lineMapper;
    }
}
