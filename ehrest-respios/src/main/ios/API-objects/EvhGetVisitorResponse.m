//
// EvhGetVisitorResponse.m
//
#import "EvhGetVisitorResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetVisitorResponse
//

@implementation EvhGetVisitorResponse

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetVisitorResponse* obj = [EvhGetVisitorResponse new];
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
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.doorName)
        [jsonObject setObject: self.doorName forKey: @"doorName"];
    if(self.behaviorTime)
        [jsonObject setObject: self.behaviorTime forKey: @"behaviorTime"];
    if(self.qr)
        [jsonObject setObject: self.qr forKey: @"qr"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.doorName = [jsonObject objectForKey: @"doorName"];
        if(self.doorName && [self.doorName isEqual:[NSNull null]])
            self.doorName = nil;

        self.behaviorTime = [jsonObject objectForKey: @"behaviorTime"];
        if(self.behaviorTime && [self.behaviorTime isEqual:[NSNull null]])
            self.behaviorTime = nil;

        self.qr = [jsonObject objectForKey: @"qr"];
        if(self.qr && [self.qr isEqual:[NSNull null]])
            self.qr = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
