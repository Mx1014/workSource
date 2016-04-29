//
// EvhVerifyAndResetPasswordCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhVerifyAndResetPasswordCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAndResetPasswordCommand
//

@implementation EvhVerifyAndResetPasswordCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhVerifyAndResetPasswordCommand* obj = [EvhVerifyAndResetPasswordCommand new];
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
    if(self.identifierToken)
        [jsonObject setObject: self.identifierToken forKey: @"identifierToken"];
    if(self.verifyCode)
        [jsonObject setObject: self.verifyCode forKey: @"verifyCode"];
    if(self.theNewPassword)
        [jsonObject setObject: self.theNewPassword forKey: @"newPassword"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.identifierToken = [jsonObject objectForKey: @"identifierToken"];
        if(self.identifierToken && [self.identifierToken isEqual:[NSNull null]])
            self.identifierToken = nil;

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
