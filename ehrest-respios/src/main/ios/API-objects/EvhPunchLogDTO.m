//
// EvhPunchLogDTO.m
//
#import "EvhPunchLogDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchLogDTO
//

@implementation EvhPunchLogDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhPunchLogDTO* obj = [EvhPunchLogDTO new];
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
    if(self.punchTime)
        [jsonObject setObject: self.punchTime forKey: @"punchTime"];
    if(self.clockStatus)
        [jsonObject setObject: self.clockStatus forKey: @"clockStatus"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.punchTime = [jsonObject objectForKey: @"punchTime"];
        if(self.punchTime && [self.punchTime isEqual:[NSNull null]])
            self.punchTime = nil;

        self.clockStatus = [jsonObject objectForKey: @"clockStatus"];
        if(self.clockStatus && [self.clockStatus isEqual:[NSNull null]])
            self.clockStatus = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
