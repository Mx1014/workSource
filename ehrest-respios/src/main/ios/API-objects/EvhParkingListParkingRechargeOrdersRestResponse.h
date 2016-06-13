//
// EvhParkingListParkingRechargeOrdersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListParkingRechargeOrdersResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingListParkingRechargeOrdersRestResponse
//
@interface EvhParkingListParkingRechargeOrdersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListParkingRechargeOrdersResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
