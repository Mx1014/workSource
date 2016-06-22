//
// EvhCreateLinglingVisitorCommand.m
//
#import "EvhCreateLinglingVisitorCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateLinglingVisitorCommand
//

@implementation EvhCreateLinglingVisitorCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateLinglingVisitorCommand* obj = [EvhCreateLinglingVisitorCommand new];
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
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.userName)
        [jsonObject setObject: self.userName forKey: @"userName"];
    if(self.visitorEvent)
        [jsonObject setObject: self.visitorEvent forKey: @"visitorEvent"];
    if(self.organization)
        [jsonObject setObject: self.organization forKey: @"organization"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.userName = [jsonObject objectForKey: @"userName"];
        if(self.userName && [self.userName isEqual:[NSNull null]])
            self.userName = nil;

        self.visitorEvent = [jsonObject objectForKey: @"visitorEvent"];
        if(self.visitorEvent && [self.visitorEvent isEqual:[NSNull null]])
            self.visitorEvent = nil;

        self.organization = [jsonObject objectForKey: @"organization"];
        if(self.organization && [self.organization isEqual:[NSNull null]])
            self.organization = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
