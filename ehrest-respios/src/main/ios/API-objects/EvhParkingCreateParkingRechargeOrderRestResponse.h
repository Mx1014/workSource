//
// EvhParkingCreateParkingRechargeOrderRestResponse.h
// generated at 2016-04-07 17:03:18 
//
#import "RestResponseBase.h"
#import "EvhParkingRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingCreateParkingRechargeOrderRestResponse
//
@interface EvhParkingCreateParkingRechargeOrderRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingRechargeOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
