//
// EvhTimeRangeDTO.m
//
#import "EvhTimeRangeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTimeRangeDTO
//

@implementation EvhTimeRangeDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhTimeRangeDTO* obj = [EvhTimeRangeDTO new];
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
    if(self.duration)
        [jsonObject setObject: self.duration forKey: @"duration"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.duration = [jsonObject objectForKey: @"duration"];
        if(self.duration && [self.duration isEqual:[NSNull null]])
            self.duration = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
