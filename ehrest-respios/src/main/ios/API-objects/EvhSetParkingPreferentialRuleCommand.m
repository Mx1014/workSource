//
// EvhSetParkingPreferentialRuleCommand.m
//
#import "EvhSetParkingPreferentialRuleCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetParkingPreferentialRuleCommand
//

@implementation EvhSetParkingPreferentialRuleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetParkingPreferentialRuleCommand* obj = [EvhSetParkingPreferentialRuleCommand new];
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
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.range)
        [jsonObject setObject: self.range forKey: @"range"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.range = [jsonObject objectForKey: @"range"];
        if(self.range && [self.range isEqual:[NSNull null]])
            self.range = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
