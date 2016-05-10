//
// EvhPmsySearchBillingOrdersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhSearchBillsOrdersResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsySearchBillingOrdersRestResponse
//
@interface EvhPmsySearchBillingOrdersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhSearchBillsOrdersResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
