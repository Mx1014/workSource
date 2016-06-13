//
// EvhVerifyAndLogonByIdentifierCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAndLogonByIdentifierCommand
//
@interface EvhVerifyAndLogonByIdentifierCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* userIdentifier;

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

