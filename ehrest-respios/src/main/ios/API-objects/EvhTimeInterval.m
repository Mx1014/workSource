//
// EvhTimeInterval.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhTimeInterval.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTimeInterval
//

@implementation EvhTimeInterval

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhTimeInterval* obj = [EvhTimeInterval new];
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
    if(self.beginTime)
        [jsonObject setObject: self.beginTime forKey: @"beginTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.beginTime = [jsonObject objectForKey: @"beginTime"];
        if(self.beginTime && [self.beginTime isEqual:[NSNull null]])
            self.beginTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
