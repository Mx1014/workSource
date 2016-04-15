//
// EvhParkingListParkingRechargeOrdersRestResponse.h
// generated at 2016-04-12 15:02:21 
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
