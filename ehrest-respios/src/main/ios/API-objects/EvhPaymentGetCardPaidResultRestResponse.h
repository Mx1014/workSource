//
// EvhPaymentGetCardPaidResultRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetCardPaidResultDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentGetCardPaidResultRestResponse
//
@interface EvhPaymentGetCardPaidResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetCardPaidResultDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
