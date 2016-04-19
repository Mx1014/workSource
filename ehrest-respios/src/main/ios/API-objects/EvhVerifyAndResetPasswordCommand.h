//
// EvhVerifyAndResetPasswordCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

