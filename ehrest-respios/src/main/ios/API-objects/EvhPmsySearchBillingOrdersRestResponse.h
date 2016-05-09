//
// EvhPmsySearchBillingOrdersRestResponse.h
// generated at 2016-04-29 18:56:04 
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
