//
// EvhPaymentGetCardPaidQrCodeRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhGetCardPaidQrCodeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentGetCardPaidQrCodeRestResponse
//
@interface EvhPaymentGetCardPaidQrCodeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetCardPaidQrCodeDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
