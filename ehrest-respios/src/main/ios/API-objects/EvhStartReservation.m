//
// EvhStartReservation.m
//
#import "EvhStartReservation.h"

///////////////////////////////////////////////////////////////////////////////
// EvhStartReservation
//

@implementation EvhStartReservation

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhStartReservation* obj = [EvhStartReservation new];
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
    if(self.loginName)
        [jsonObject setObject: self.loginName forKey: @"loginName"];
    if(self.timeStamp)
        [jsonObject setObject: self.timeStamp forKey: @"timeStamp"];
    if(self.token)
        [jsonObject setObject: self.token forKey: @"token"];
    if(self.confName)
        [jsonObject setObject: self.confName forKey: @"confName"];
    if(self.hostKey)
        [jsonObject setObject: self.hostKey forKey: @"hostKey"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.duration)
        [jsonObject setObject: self.duration forKey: @"duration"];
    if(self.optionJbh)
        [jsonObject setObject: self.optionJbh forKey: @"optionJbh"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.loginName = [jsonObject objectForKey: @"loginName"];
        if(self.loginName && [self.loginName isEqual:[NSNull null]])
            self.loginName = nil;

        self.timeStamp = [jsonObject objectForKey: @"timeStamp"];
        if(self.timeStamp && [self.timeStamp isEqual:[NSNull null]])
            self.timeStamp = nil;

        self.token = [jsonObject objectForKey: @"token"];
        if(self.token && [self.token isEqual:[NSNull null]])
            self.token = nil;

        self.confName = [jsonObject objectForKey: @"confName"];
        if(self.confName && [self.confName isEqual:[NSNull null]])
            self.confName = nil;

        self.hostKey = [jsonObject objectForKey: @"hostKey"];
        if(self.hostKey && [self.hostKey isEqual:[NSNull null]])
            self.hostKey = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.duration = [jsonObject objectForKey: @"duration"];
        if(self.duration && [self.duration isEqual:[NSNull null]])
            self.duration = nil;

        self.optionJbh = [jsonObject objectForKey: @"optionJbh"];
        if(self.optionJbh && [self.optionJbh isEqual:[NSNull null]])
            self.optionJbh = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
