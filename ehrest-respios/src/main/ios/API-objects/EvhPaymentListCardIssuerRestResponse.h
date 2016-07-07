//
// EvhPaymentListCardIssuerRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentListCardIssuerRestResponse
//
@interface EvhPaymentListCardIssuerRestResponse : EvhRestResponseBase

// array of EvhCardIssuerDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
