//
// EvhParkingSearchParkingRechargeOrdersRestResponse.h
// generated at 2016-04-18 14:48:52 
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
