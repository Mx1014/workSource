package com.everhomes.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.bootstrap.PlatformContext;
import com.everhomes.region.Region;
import com.everhomes.region.RegionProvider;
import com.everhomes.rest.region.RegionScope;

//public class SimpleJobTest implements ScheduleJob {
//    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");
//    
//    @Autowired
//    private RegionProvider regionProvider;
//        
//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        Map<String, Object> map = new HashMap<String, Object>();
//        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
//        Iterator<Entry<String, Object>> iterator = jobMap.entrySet().iterator();
//        while(iterator.hasNext()) {
//            Entry<String, Object> entry = iterator.next();
//            map.put(entry.getKey(), entry.getValue());
//        }
//        //RegionProvider regionProvider = PlatformContext.getComponent(RegionProvider.class);
//        List<Region> regions = regionProvider.listActiveRegion(RegionScope.CITY);
//        List<String> regionNames = regions.stream().map((r) -> {
//            return r.getName();
//        }).collect(Collectors.toList());
//        LOGGER.info("Execute at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ", map=" + map + ", regionNames=" + regionNames);
//    }
//
//}

@Component
@Scope("prototype")
public class SimpleJobTest extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger("schedulelog");
    
    @Autowired
    private RegionProvider regionProvider;

    @Override
    protected void executeInternal(JobExecutionContext context) {
        Map<String, Object> map = new HashMap<String, Object>();
        JobDataMap jobMap = context.getJobDetail().getJobDataMap();
        Iterator<Entry<String, Object>> iterator = jobMap.entrySet().iterator();
        while(iterator.hasNext()) {
            Entry<String, Object> entry = iterator.next();
            map.put(entry.getKey(), entry.getValue());
        }
        //RegionProvider regionProvider = PlatformContext.getComponent(RegionProvider.class);
        List<Region> regions = regionProvider.listActiveRegion(RegionScope.CITY);
        List<String> regionNames = regions.stream().map((r) -> {
            return r.getName();
        }).collect(Collectors.toList());
        LOGGER.info("Execute at " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + ", map=" + map + ", regionNames=" + regionNames);
    }
}
