//
// EvhParkingCreateParkingRechargeRateRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"
#import "EvhParkingRechargeRateDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingCreateParkingRechargeRateRestResponse
//
@interface EvhParkingCreateParkingRechargeRateRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingRechargeRateDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
