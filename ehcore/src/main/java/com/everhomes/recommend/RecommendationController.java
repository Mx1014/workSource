package com.everhomes.recommend;

import javax.annotation.PostConstruct;
import javax.validation.Valid;

import net.greghaines.jesque.Job;

import org.elasticsearch.cluster.metadata.MappingMetaData.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.everhomes.constants.ErrorCodes;
import com.everhomes.controller.ControllerBase;
import com.everhomes.discover.RestDoc;
import com.everhomes.discover.RestReturn;
import com.everhomes.rest.RestResponse;
import com.everhomes.search.SearchSyncAction;
import com.everhomes.taskqueue.CommonWorkerPool;
import com.everhomes.taskqueue.JesqueClientFactory;
import com.everhomes.util.ConvertHelper;

@RestDoc(value="Recommendation", site="ehcore")
@RestController
@RequestMapping("/recommend")
public class RecommendationController extends ControllerBase {
    @Autowired
    RecommendationConfigProvider recommendationConfigProvider;
    
    @Autowired
    private CommonWorkerPool workerPool;
    
    @Autowired
    JesqueClientFactory jesqueClientFactory;
    
    private final String queueName = "recommend";
    
    @PostConstruct
    public void setup() {
        workerPool.addQueue(queueName);
    }
    
    @RequestMapping("/admin/recommend/createConfig")
    @RestReturn(value=String.class)
    public RestResponse createConfig(@Valid CreateRecommendConfig cmd) {
        RestResponse response = new RestResponse();
        RecommendationConfig config = ConvertHelper.convert(cmd, RecommendationConfig.class);
        config.setCreateTime(DateTimeUtils.fromString(cmd.getExpireTime()));
        
        recommendationConfigProvider.createRecommendConfig(config);
        if(config.getId() > 0) {
            final Job job = new Job(RecommendAction.class.getName(),
                    new Object[]{ config });
            
            jesqueClientFactory.getClientPool().enqueue(queueName, job);
            
            response.setErrorCode(ErrorCodes.SUCCESS);
            response.setErrorDescription("OK");    
        } else {
            response.setErrorCode(ErrorCodes.ERROR_GENERAL_EXCEPTION);
            response.setErrorDescription("Cannot create suggest config");
        }
        
        return response;
    }
}
