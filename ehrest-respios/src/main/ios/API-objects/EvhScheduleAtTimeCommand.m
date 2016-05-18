//
// EvhScheduleAtTimeCommand.m
//
#import "EvhScheduleAtTimeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhScheduleAtTimeCommand
//

@implementation EvhScheduleAtTimeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhScheduleAtTimeCommand* obj = [EvhScheduleAtTimeCommand new];
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
