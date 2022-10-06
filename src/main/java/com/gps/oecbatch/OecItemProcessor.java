package com.gps.oecbatch;

import com.gps.model.OecEntity;
import org.springframework.batch.item.ItemProcessor;

public class OecItemProcessor implements ItemProcessor<OecEntity,OecEntity> {

    @Override
    public OecEntity process(OecEntity oecEntity) throws Exception {
        return oecEntity;
    }
}
