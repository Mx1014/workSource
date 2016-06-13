//
// EvhWaitingDaysResponse.m
//
#import "EvhWaitingDaysResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWaitingDaysResponse
//

@implementation EvhWaitingDaysResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhWaitingDaysResponse* obj = [EvhWaitingDaysResponse new];
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
    if(self.waitingDays)
        [jsonObject setObject: self.waitingDays forKey: @"waitingDays"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.waitingDays = [jsonObject objectForKey: @"waitingDays"];
        if(self.waitingDays && [self.waitingDays isEqual:[NSNull null]])
            self.waitingDays = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
