//
// EvhPaymentSendCardVerifyCodeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSendCardVerifyCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentSendCardVerifyCodeRestResponse
//
@interface EvhPaymentSendCardVerifyCodeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSendCardVerifyCodeDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
