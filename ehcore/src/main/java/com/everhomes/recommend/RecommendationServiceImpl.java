package com.everhomes.recommend;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import net.greghaines.jesque.Job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.everhomes.taskqueue.CommonWorkerPool;
import com.everhomes.taskqueue.JesqueClientFactory;
import com.everhomes.util.ConvertHelper;

@Service
public class RecommendationServiceImpl implements RecommendationService {
    @Autowired
    private CommonWorkerPool workerPool;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    @Autowired
    RecommendationConfigProvider recommendationConfigProvider;
    
    @Autowired
    RecommendationProvider recommendationProvider;
    
    private final String queueName = "recommend";
    
    @PostConstruct
    public void setup() {
        workerPool.addQueue(queueName);
    }
    
    @Override
    public RecommendationConfig createConfig(@Valid CreateRecommendConfig cmd) {
       RecommendationConfig config = ConvertHelper.convert(cmd, RecommendationConfig.class);
       config.setCreateTime(DateTimeUtils.fromString(cmd.getExpireTime()));
       
       recommendationConfigProvider.createRecommendConfig(config);
       if(config.getId() > 0) {
           final Job job = new Job(RecommendAction.class.getName(),
                   new Object[]{ config });
           
           jesqueClientFactory.getClientPool().enqueue(queueName, job);
       }
       
       return config;
    }
}
