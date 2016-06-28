//
// EvhSendCardVerifyCodeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSendCardVerifyCodeDTO
//
@interface EvhSendCardVerifyCodeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* verifyCode;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

