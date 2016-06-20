//
// EvhPaymentSearchCardTransactionsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSearchCardTransactionsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentSearchCardTransactionsRestResponse
//
@interface EvhPaymentSearchCardTransactionsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchCardTransactionsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
