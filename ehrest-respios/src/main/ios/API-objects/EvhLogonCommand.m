//
// EvhLogonCommand.m
//
#import "EvhLogonCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLogonCommand
//

@implementation EvhLogonCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhLogonCommand* obj = [EvhLogonCommand new];
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
    if(self.userIdentifier)
        [jsonObject setObject: self.userIdentifier forKey: @"userIdentifier"];
    if(self.password)
        [jsonObject setObject: self.password forKey: @"password"];
    if(self.deviceIdentifier)
        [jsonObject setObject: self.deviceIdentifier forKey: @"deviceIdentifier"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.pusherIdentify)
        [jsonObject setObject: self.pusherIdentify forKey: @"pusherIdentify"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userIdentifier = [jsonObject objectForKey: @"userIdentifier"];
        if(self.userIdentifier && [self.userIdentifier isEqual:[NSNull null]])
            self.userIdentifier = nil;

        self.password = [jsonObject objectForKey: @"password"];
        if(self.password && [self.password isEqual:[NSNull null]])
            self.password = nil;

        self.deviceIdentifier = [jsonObject objectForKey: @"deviceIdentifier"];
        if(self.deviceIdentifier && [self.deviceIdentifier isEqual:[NSNull null]])
            self.deviceIdentifier = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.pusherIdentify = [jsonObject objectForKey: @"pusherIdentify"];
        if(self.pusherIdentify && [self.pusherIdentify isEqual:[NSNull null]])
            self.pusherIdentify = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
