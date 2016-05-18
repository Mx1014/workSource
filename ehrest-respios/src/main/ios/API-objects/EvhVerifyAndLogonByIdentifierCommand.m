//
// EvhVerifyAndLogonByIdentifierCommand.m
//
#import "EvhVerifyAndLogonByIdentifierCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAndLogonByIdentifierCommand
//

@implementation EvhVerifyAndLogonByIdentifierCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyAndLogonByIdentifierCommand* obj = [EvhVerifyAndLogonByIdentifierCommand new];
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
    if(self.verificationCode)
        [jsonObject setObject: self.verificationCode forKey: @"verificationCode"];
    if(self.initialPassword)
        [jsonObject setObject: self.initialPassword forKey: @"initialPassword"];
    if(self.nickName)
        [jsonObject setObject: self.nickName forKey: @"nickName"];
    if(self.invitationCode)
        [jsonObject setObject: self.invitationCode forKey: @"invitationCode"];
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

        self.verificationCode = [jsonObject objectForKey: @"verificationCode"];
        if(self.verificationCode && [self.verificationCode isEqual:[NSNull null]])
            self.verificationCode = nil;

        self.initialPassword = [jsonObject objectForKey: @"initialPassword"];
        if(self.initialPassword && [self.initialPassword isEqual:[NSNull null]])
            self.initialPassword = nil;

        self.nickName = [jsonObject objectForKey: @"nickName"];
        if(self.nickName && [self.nickName isEqual:[NSNull null]])
            self.nickName = nil;

        self.invitationCode = [jsonObject objectForKey: @"invitationCode"];
        if(self.invitationCode && [self.invitationCode isEqual:[NSNull null]])
            self.invitationCode = nil;

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
