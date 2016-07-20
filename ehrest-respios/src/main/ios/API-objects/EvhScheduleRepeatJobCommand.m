//
// EvhScheduleRepeatJobCommand.m
//
#import "EvhScheduleRepeatJobCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleRepeatJobCommand
//

@implementation EvhScheduleRepeatJobCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhScheduleRepeatJobCommand* obj = [EvhScheduleRepeatJobCommand new];
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
    if(self.triggerName)
        [jsonObject setObject: self.triggerName forKey: @"triggerName"];
    if(self.jobName)
        [jsonObject setObject: self.jobName forKey: @"jobName"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.repeatInterval)
        [jsonObject setObject: self.repeatInterval forKey: @"repeatInterval"];
    if(self.repeatCount)
        [jsonObject setObject: self.repeatCount forKey: @"repeatCount"];
    if(self.jobClassName)
        [jsonObject setObject: self.jobClassName forKey: @"jobClassName"];
    if(self.parameterJson)
        [jsonObject setObject: self.parameterJson forKey: @"parameterJson"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.triggerName = [jsonObject objectForKey: @"triggerName"];
        if(self.triggerName && [self.triggerName isEqual:[NSNull null]])
            self.triggerName = nil;

        self.jobName = [jsonObject objectForKey: @"jobName"];
        if(self.jobName && [self.jobName isEqual:[NSNull null]])
            self.jobName = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.repeatInterval = [jsonObject objectForKey: @"repeatInterval"];
        if(self.repeatInterval && [self.repeatInterval isEqual:[NSNull null]])
            self.repeatInterval = nil;

        self.repeatCount = [jsonObject objectForKey: @"repeatCount"];
        if(self.repeatCount && [self.repeatCount isEqual:[NSNull null]])
            self.repeatCount = nil;

        self.jobClassName = [jsonObject objectForKey: @"jobClassName"];
        if(self.jobClassName && [self.jobClassName isEqual:[NSNull null]])
            self.jobClassName = nil;

        self.parameterJson = [jsonObject objectForKey: @"parameterJson"];
        if(self.parameterJson && [self.parameterJson isEqual:[NSNull null]])
            self.parameterJson = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
