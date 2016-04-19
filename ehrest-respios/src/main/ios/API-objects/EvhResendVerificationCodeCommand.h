//
// EvhResendVerificationCodeCommand.h
// generated at 2016-04-19 13:40:01 
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

