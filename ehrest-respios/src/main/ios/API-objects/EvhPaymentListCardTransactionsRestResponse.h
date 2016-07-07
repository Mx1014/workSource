//
// EvhPaymentListCardTransactionsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListCardTransactionsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentListCardTransactionsRestResponse
//
@interface EvhPaymentListCardTransactionsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListCardTransactionsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
