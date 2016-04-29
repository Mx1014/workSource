//
// EvhPunchLogDTO.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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
