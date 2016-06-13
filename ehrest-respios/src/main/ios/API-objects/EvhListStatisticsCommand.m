//
// EvhListStatisticsCommand.m
//
#import "EvhListStatisticsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsCommand
//

@implementation EvhListStatisticsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsCommand* obj = [EvhListStatisticsCommand new];
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
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.stopTime)
        [jsonObject setObject: self.stopTime forKey: @"stopTime"];
    if(self.channelId)
        [jsonObject setObject: self.channelId forKey: @"channelId"];
    if(self.cityId)
        [jsonObject setObject: self.cityId forKey: @"cityId"];
    if(self.communityId)
        [jsonObject setObject: self.communityId forKey: @"communityId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.stopTime = [jsonObject objectForKey: @"stopTime"];
        if(self.stopTime && [self.stopTime isEqual:[NSNull null]])
            self.stopTime = nil;

        self.channelId = [jsonObject objectForKey: @"channelId"];
        if(self.channelId && [self.channelId isEqual:[NSNull null]])
            self.channelId = nil;

        self.cityId = [jsonObject objectForKey: @"cityId"];
        if(self.cityId && [self.cityId isEqual:[NSNull null]])
            self.cityId = nil;

        self.communityId = [jsonObject objectForKey: @"communityId"];
        if(self.communityId && [self.communityId isEqual:[NSNull null]])
            self.communityId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
