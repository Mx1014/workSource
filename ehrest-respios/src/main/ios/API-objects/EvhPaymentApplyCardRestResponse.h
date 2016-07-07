//
// EvhPaymentApplyCardRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhCardInfoDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentApplyCardRestResponse
//
@interface EvhPaymentApplyCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhCardInfoDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
