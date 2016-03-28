//
// EvhResendVerificationCodeCommand.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhResendVerificationCodeCommand
//
@interface EvhResendVerificationCodeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* signupToken;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

