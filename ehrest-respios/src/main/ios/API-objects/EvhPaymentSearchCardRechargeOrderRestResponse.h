//
// EvhPaymentSearchCardRechargeOrderRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSearchCardRechargeOrderResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentSearchCardRechargeOrderRestResponse
//
@interface EvhPaymentSearchCardRechargeOrderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchCardRechargeOrderResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
