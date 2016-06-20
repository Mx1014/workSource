//
// EvhPaymentNotifyPaidResultRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhNotifyEntityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentNotifyPaidResultRestResponse
//
@interface EvhPaymentNotifyPaidResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhNotifyEntityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
