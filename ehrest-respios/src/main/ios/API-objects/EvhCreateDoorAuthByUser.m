//
// EvhCreateDoorAuthByUser.m
//
#import "EvhCreateDoorAuthByUser.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDoorAuthByUser
//

@implementation EvhCreateDoorAuthByUser

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCreateDoorAuthByUser* obj = [EvhCreateDoorAuthByUser new];
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
    if(self.authType)
        [jsonObject setObject: self.authType forKey: @"authType"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.validFromMs)
        [jsonObject setObject: self.validFromMs forKey: @"validFromMs"];
    if(self.validEndMs)
        [jsonObject setObject: self.validEndMs forKey: @"validEndMs"];
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

        self.authType = [jsonObject objectForKey: @"authType"];
        if(self.authType && [self.authType isEqual:[NSNull null]])
            self.authType = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.validFromMs = [jsonObject objectForKey: @"validFromMs"];
        if(self.validFromMs && [self.validFromMs isEqual:[NSNull null]])
            self.validFromMs = nil;

        self.validEndMs = [jsonObject objectForKey: @"validEndMs"];
        if(self.validEndMs && [self.validEndMs isEqual:[NSNull null]])
            self.validEndMs = nil;

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
