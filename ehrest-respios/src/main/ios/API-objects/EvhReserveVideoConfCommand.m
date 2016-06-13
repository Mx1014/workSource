//
// EvhReserveVideoConfCommand.m
//
#import "EvhReserveVideoConfCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReserveVideoConfCommand
//

@implementation EvhReserveVideoConfCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhReserveVideoConfCommand* obj = [EvhReserveVideoConfCommand new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.subject)
        [jsonObject setObject: self.subject forKey: @"subject"];
    if(self.timeZone)
        [jsonObject setObject: self.timeZone forKey: @"timeZone"];
    if(self.startTime)
        [jsonObject setObject: self.startTime forKey: @"startTime"];
    if(self.endTime)
        [jsonObject setObject: self.endTime forKey: @"endTime"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.hostKey)
        [jsonObject setObject: self.hostKey forKey: @"hostKey"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.subject = [jsonObject objectForKey: @"subject"];
        if(self.subject && [self.subject isEqual:[NSNull null]])
            self.subject = nil;

        self.timeZone = [jsonObject objectForKey: @"timeZone"];
        if(self.timeZone && [self.timeZone isEqual:[NSNull null]])
            self.timeZone = nil;

        self.startTime = [jsonObject objectForKey: @"startTime"];
        if(self.startTime && [self.startTime isEqual:[NSNull null]])
            self.startTime = nil;

        self.endTime = [jsonObject objectForKey: @"endTime"];
        if(self.endTime && [self.endTime isEqual:[NSNull null]])
            self.endTime = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        self.hostKey = [jsonObject objectForKey: @"hostKey"];
        if(self.hostKey && [self.hostKey isEqual:[NSNull null]])
            self.hostKey = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
