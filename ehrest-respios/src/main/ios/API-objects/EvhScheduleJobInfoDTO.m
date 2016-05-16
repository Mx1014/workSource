//
// EvhScheduleJobInfoDTO.m
//
#import "EvhScheduleJobInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleJobInfoDTO
//

@implementation EvhScheduleJobInfoDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhScheduleJobInfoDTO* obj = [EvhScheduleJobInfoDTO new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.triggerGroupName)
        [jsonObject setObject: self.triggerGroupName forKey: @"triggerGroupName"];
    if(self.triggerName)
        [jsonObject setObject: self.triggerName forKey: @"triggerName"];
    if(self.triggerType)
        [jsonObject setObject: self.triggerType forKey: @"triggerType"];
    if(self.triggerState)
        [jsonObject setObject: self.triggerState forKey: @"triggerState"];
    if(self.jobGroupName)
        [jsonObject setObject: self.jobGroupName forKey: @"jobGroupName"];
    if(self.jobName)
        [jsonObject setObject: self.jobName forKey: @"jobName"];
    if(self.cronExpression)
        [jsonObject setObject: self.cronExpression forKey: @"cronExpression"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.previousFireTime)
        [jsonObject setObject: self.previousFireTime forKey: @"previousFireTime"];
    if(self.nextFireTime)
        [jsonObject setObject: self.nextFireTime forKey: @"nextFireTime"];
    if(self.finalFireTime)
        [jsonObject setObject: self.finalFireTime forKey: @"finalFireTime"];
    if(self.misfireInstruction)
        [jsonObject setObject: self.misfireInstruction forKey: @"misfireInstruction"];
    if(self.repeatInterval)
        [jsonObject setObject: self.repeatInterval forKey: @"repeatInterval"];
    if(self.repeatCount)
        [jsonObject setObject: self.repeatCount forKey: @"repeatCount"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.triggerGroupName = [jsonObject objectForKey: @"triggerGroupName"];
        if(self.triggerGroupName && [self.triggerGroupName isEqual:[NSNull null]])
            self.triggerGroupName = nil;

        self.triggerName = [jsonObject objectForKey: @"triggerName"];
        if(self.triggerName && [self.triggerName isEqual:[NSNull null]])
            self.triggerName = nil;

        self.triggerType = [jsonObject objectForKey: @"triggerType"];
        if(self.triggerType && [self.triggerType isEqual:[NSNull null]])
            self.triggerType = nil;

        self.triggerState = [jsonObject objectForKey: @"triggerState"];
        if(self.triggerState && [self.triggerState isEqual:[NSNull null]])
            self.triggerState = nil;

        self.jobGroupName = [jsonObject objectForKey: @"jobGroupName"];
        if(self.jobGroupName && [self.jobGroupName isEqual:[NSNull null]])
            self.jobGroupName = nil;

        self.jobName = [jsonObject objectForKey: @"jobName"];
        if(self.jobName && [self.jobName isEqual:[NSNull null]])
            self.jobName = nil;

        self.cronExpression = [jsonObject objectForKey: @"cronExpression"];
        if(self.cronExpression && [self.cronExpression isEqual:[NSNull null]])
            self.cronExpression = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.previousFireTime = [jsonObject objectForKey: @"previousFireTime"];
        if(self.previousFireTime && [self.previousFireTime isEqual:[NSNull null]])
            self.previousFireTime = nil;

        self.nextFireTime = [jsonObject objectForKey: @"nextFireTime"];
        if(self.nextFireTime && [self.nextFireTime isEqual:[NSNull null]])
            self.nextFireTime = nil;

        self.finalFireTime = [jsonObject objectForKey: @"finalFireTime"];
        if(self.finalFireTime && [self.finalFireTime isEqual:[NSNull null]])
            self.finalFireTime = nil;

        self.misfireInstruction = [jsonObject objectForKey: @"misfireInstruction"];
        if(self.misfireInstruction && [self.misfireInstruction isEqual:[NSNull null]])
            self.misfireInstruction = nil;

        self.repeatInterval = [jsonObject objectForKey: @"repeatInterval"];
        if(self.repeatInterval && [self.repeatInterval isEqual:[NSNull null]])
            self.repeatInterval = nil;

        self.repeatCount = [jsonObject objectForKey: @"repeatCount"];
        if(self.repeatCount && [self.repeatCount isEqual:[NSNull null]])
            self.repeatCount = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
