//
// EvhPunchClockResponse.m
//
#import "EvhPunchClockResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchClockResponse
//

@implementation EvhPunchClockResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchClockResponse* obj = [EvhPunchClockResponse new];
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
    if(self.punchCode)
        [jsonObject setObject: self.punchCode forKey: @"punchCode"];
    if(self.punchTime)
        [jsonObject setObject: self.punchTime forKey: @"punchTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.punchCode = [jsonObject objectForKey: @"punchCode"];
        if(self.punchCode && [self.punchCode isEqual:[NSNull null]])
            self.punchCode = nil;

        self.punchTime = [jsonObject objectForKey: @"punchTime"];
        if(self.punchTime && [self.punchTime isEqual:[NSNull null]])
            self.punchTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
