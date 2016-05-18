//
// EvhRepeatSettingsDTO.m
//
#import "EvhRepeatSettingsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRepeatSettingsDTO
//

@implementation EvhRepeatSettingsDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRepeatSettingsDTO* obj = [EvhRepeatSettingsDTO new];
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
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.foreverFlag)
        [jsonObject setObject: self.foreverFlag forKey: @"foreverFlag"];
    if(self.repeatCount)
        [jsonObject setObject: self.repeatCount forKey: @"repeatCount"];
    if(self.startDate)
        [jsonObject setObject: self.startDate forKey: @"startDate"];
    if(self.endDate)
        [jsonObject setObject: self.endDate forKey: @"endDate"];
    if(self.timeRanges)
        [jsonObject setObject: self.timeRanges forKey: @"timeRanges"];
    if(self.repeatType)
        [jsonObject setObject: self.repeatType forKey: @"repeatType"];
    if(self.repeatInterval)
        [jsonObject setObject: self.repeatInterval forKey: @"repeatInterval"];
    if(self.workDayFlag)
        [jsonObject setObject: self.workDayFlag forKey: @"workDayFlag"];
    if(self.expression)
        [jsonObject setObject: self.expression forKey: @"expression"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.foreverFlag = [jsonObject objectForKey: @"foreverFlag"];
        if(self.foreverFlag && [self.foreverFlag isEqual:[NSNull null]])
            self.foreverFlag = nil;

        self.repeatCount = [jsonObject objectForKey: @"repeatCount"];
        if(self.repeatCount && [self.repeatCount isEqual:[NSNull null]])
            self.repeatCount = nil;

        self.startDate = [jsonObject objectForKey: @"startDate"];
        if(self.startDate && [self.startDate isEqual:[NSNull null]])
            self.startDate = nil;

        self.endDate = [jsonObject objectForKey: @"endDate"];
        if(self.endDate && [self.endDate isEqual:[NSNull null]])
            self.endDate = nil;

        self.timeRanges = [jsonObject objectForKey: @"timeRanges"];
        if(self.timeRanges && [self.timeRanges isEqual:[NSNull null]])
            self.timeRanges = nil;

        self.repeatType = [jsonObject objectForKey: @"repeatType"];
        if(self.repeatType && [self.repeatType isEqual:[NSNull null]])
            self.repeatType = nil;

        self.repeatInterval = [jsonObject objectForKey: @"repeatInterval"];
        if(self.repeatInterval && [self.repeatInterval isEqual:[NSNull null]])
            self.repeatInterval = nil;

        self.workDayFlag = [jsonObject objectForKey: @"workDayFlag"];
        if(self.workDayFlag && [self.workDayFlag isEqual:[NSNull null]])
            self.workDayFlag = nil;

        self.expression = [jsonObject objectForKey: @"expression"];
        if(self.expression && [self.expression isEqual:[NSNull null]])
            self.expression = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
