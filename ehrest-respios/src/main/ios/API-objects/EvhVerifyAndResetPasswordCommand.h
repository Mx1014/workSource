//
// EvhVerifyAndResetPasswordCommand.h
// generated at 2016-04-07 10:47:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVerifyAndResetPasswordCommand
//
@interface EvhVerifyAndResetPasswordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* identifierToken;

@property(nonatomic, copy) NSString* verifyCode;

@property(nonatomic, copy) NSString* theNewPassword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

