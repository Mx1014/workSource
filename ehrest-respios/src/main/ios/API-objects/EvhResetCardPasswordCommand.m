//
// EvhResetCardPasswordCommand.m
//
#import "EvhResetCardPasswordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhResetCardPasswordCommand
//

@implementation EvhResetCardPasswordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhResetCardPasswordCommand* obj = [EvhResetCardPasswordCommand new];
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
    if(self.ownerType)
        [jsonObject setObject: self.ownerType forKey: @"ownerType"];
    if(self.ownerId)
        [jsonObject setObject: self.ownerId forKey: @"ownerId"];
    if(self.mobile)
        [jsonObject setObject: self.mobile forKey: @"mobile"];
    if(self.cardId)
        [jsonObject setObject: self.cardId forKey: @"cardId"];
    if(self.verifyCode)
        [jsonObject setObject: self.verifyCode forKey: @"verifyCode"];
    if(self.theNewPassword)
        [jsonObject setObject: self.theNewPassword forKey: @"newPassword"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.ownerType = [jsonObject objectForKey: @"ownerType"];
        if(self.ownerType && [self.ownerType isEqual:[NSNull null]])
            self.ownerType = nil;

        self.ownerId = [jsonObject objectForKey: @"ownerId"];
        if(self.ownerId && [self.ownerId isEqual:[NSNull null]])
            self.ownerId = nil;

        self.mobile = [jsonObject objectForKey: @"mobile"];
        if(self.mobile && [self.mobile isEqual:[NSNull null]])
            self.mobile = nil;

        self.cardId = [jsonObject objectForKey: @"cardId"];
        if(self.cardId && [self.cardId isEqual:[NSNull null]])
            self.cardId = nil;

        self.verifyCode = [jsonObject objectForKey: @"verifyCode"];
        if(self.verifyCode && [self.verifyCode isEqual:[NSNull null]])
            self.verifyCode = nil;

        self.theNewPassword = [jsonObject objectForKey: @"newPassword"];
        if(self.theNewPassword && [self.theNewPassword isEqual:[NSNull null]])
            self.theNewPassword = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
