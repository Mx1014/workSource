//
// EvhUserLoginDTO.m
//
#import "EvhUserLoginDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserLoginDTO
//

@implementation EvhUserLoginDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhUserLoginDTO* obj = [EvhUserLoginDTO new];
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
    if(self.userId)
        [jsonObject setObject: self.userId forKey: @"userId"];
    if(self.loginId)
        [jsonObject setObject: self.loginId forKey: @"loginId"];
    if(self.deviceIdentifier)
        [jsonObject setObject: self.deviceIdentifier forKey: @"deviceIdentifier"];
    if(self.namespaceId)
        [jsonObject setObject: self.namespaceId forKey: @"namespaceId"];
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.loginBorderId)
        [jsonObject setObject: self.loginBorderId forKey: @"loginBorderId"];
    if(self.loginInstanceNumber)
        [jsonObject setObject: self.loginInstanceNumber forKey: @"loginInstanceNumber"];
    if(self.lastAccessTick)
        [jsonObject setObject: self.lastAccessTick forKey: @"lastAccessTick"];
    if(self.portalRole)
        [jsonObject setObject: self.portalRole forKey: @"portalRole"];
    if(self.partnerId)
        [jsonObject setObject: self.partnerId forKey: @"partnerId"];
    if(self.pusherIdentify)
        [jsonObject setObject: self.pusherIdentify forKey: @"pusherIdentify"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.userId = [jsonObject objectForKey: @"userId"];
        if(self.userId && [self.userId isEqual:[NSNull null]])
            self.userId = nil;

        self.loginId = [jsonObject objectForKey: @"loginId"];
        if(self.loginId && [self.loginId isEqual:[NSNull null]])
            self.loginId = nil;

        self.deviceIdentifier = [jsonObject objectForKey: @"deviceIdentifier"];
        if(self.deviceIdentifier && [self.deviceIdentifier isEqual:[NSNull null]])
            self.deviceIdentifier = nil;

        self.namespaceId = [jsonObject objectForKey: @"namespaceId"];
        if(self.namespaceId && [self.namespaceId isEqual:[NSNull null]])
            self.namespaceId = nil;

        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.loginBorderId = [jsonObject objectForKey: @"loginBorderId"];
        if(self.loginBorderId && [self.loginBorderId isEqual:[NSNull null]])
            self.loginBorderId = nil;

        self.loginInstanceNumber = [jsonObject objectForKey: @"loginInstanceNumber"];
        if(self.loginInstanceNumber && [self.loginInstanceNumber isEqual:[NSNull null]])
            self.loginInstanceNumber = nil;

        self.lastAccessTick = [jsonObject objectForKey: @"lastAccessTick"];
        if(self.lastAccessTick && [self.lastAccessTick isEqual:[NSNull null]])
            self.lastAccessTick = nil;

        self.portalRole = [jsonObject objectForKey: @"portalRole"];
        if(self.portalRole && [self.portalRole isEqual:[NSNull null]])
            self.portalRole = nil;

        self.partnerId = [jsonObject objectForKey: @"partnerId"];
        if(self.partnerId && [self.partnerId isEqual:[NSNull null]])
            self.partnerId = nil;

        self.pusherIdentify = [jsonObject objectForKey: @"pusherIdentify"];
        if(self.pusherIdentify && [self.pusherIdentify isEqual:[NSNull null]])
            self.pusherIdentify = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
