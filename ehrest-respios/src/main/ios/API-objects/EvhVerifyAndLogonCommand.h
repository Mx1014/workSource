//
// EvhVerifyAndLogonCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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

