//
// EvhListStatisticsByChannelDTO.m
//
#import "EvhListStatisticsByChannelDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListStatisticsByChannelDTO
//

@implementation EvhListStatisticsByChannelDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhListStatisticsByChannelDTO* obj = [EvhListStatisticsByChannelDTO new];
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
    if(self.channel)
        [jsonObject setObject: self.channel forKey: @"channel"];
    if(self.activeCount)
        [jsonObject setObject: self.activeCount forKey: @"activeCount"];
    if(self.registerConut)
        [jsonObject setObject: self.registerConut forKey: @"registerConut"];
    if(self.regRatio)
        [jsonObject setObject: self.regRatio forKey: @"regRatio"];
    if(self.channelActiveRatio)
        [jsonObject setObject: self.channelActiveRatio forKey: @"channelActiveRatio"];
    if(self.channelRegRatio)
        [jsonObject setObject: self.channelRegRatio forKey: @"channelRegRatio"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.channel = [jsonObject objectForKey: @"channel"];
        if(self.channel && [self.channel isEqual:[NSNull null]])
            self.channel = nil;

        self.activeCount = [jsonObject objectForKey: @"activeCount"];
        if(self.activeCount && [self.activeCount isEqual:[NSNull null]])
            self.activeCount = nil;

        self.registerConut = [jsonObject objectForKey: @"registerConut"];
        if(self.registerConut && [self.registerConut isEqual:[NSNull null]])
            self.registerConut = nil;

        self.regRatio = [jsonObject objectForKey: @"regRatio"];
        if(self.regRatio && [self.regRatio isEqual:[NSNull null]])
            self.regRatio = nil;

        self.channelActiveRatio = [jsonObject objectForKey: @"channelActiveRatio"];
        if(self.channelActiveRatio && [self.channelActiveRatio isEqual:[NSNull null]])
            self.channelActiveRatio = nil;

        self.channelRegRatio = [jsonObject objectForKey: @"channelRegRatio"];
        if(self.channelRegRatio && [self.channelRegRatio isEqual:[NSNull null]])
            self.channelRegRatio = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
