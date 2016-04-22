//
// EvhResendVerificationCodeByIdentifierCommand.h
// generated at 2016-04-22 13:56:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhResendVerificationCodeByIdentifierCommand
//
@interface EvhResendVerificationCodeByIdentifierCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* identifier;

@property(nonatomic, copy) NSNumber* namespaceId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

