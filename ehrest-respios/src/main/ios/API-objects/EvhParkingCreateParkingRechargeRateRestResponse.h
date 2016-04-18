//
// EvhParkingCreateParkingRechargeRateRestResponse.h
// generated at 2016-04-18 14:48:52 
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
