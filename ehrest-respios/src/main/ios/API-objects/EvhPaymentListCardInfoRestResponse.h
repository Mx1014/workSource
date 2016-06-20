//
// EvhPaymentListCardInfoRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPaymentListCardInfoRestResponse
//
@interface EvhPaymentListCardInfoRestResponse : EvhRestResponseBase

// array of EvhCardInfoDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
