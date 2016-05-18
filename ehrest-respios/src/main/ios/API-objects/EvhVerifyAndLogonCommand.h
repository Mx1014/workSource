//
// EvhVerifyAndLogonCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAndLogonCommand
//
@interface EvhVerifyAndLogonCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* signupToken;

@property(nonatomic, copy) NSString* verificationCode;

@property(nonatomic, copy) NSString* initialPassword;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* invitationCode;

@property(nonatomic, copy) NSString* deviceIdentifier;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* pusherIdentify;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

