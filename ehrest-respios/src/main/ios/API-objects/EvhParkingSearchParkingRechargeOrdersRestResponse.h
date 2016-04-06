//
// EvhParkingSearchParkingRechargeOrdersRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListParkingRechargeOrdersResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingSearchParkingRechargeOrdersRestResponse
//
@interface EvhParkingSearchParkingRechargeOrdersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListParkingRechargeOrdersResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
