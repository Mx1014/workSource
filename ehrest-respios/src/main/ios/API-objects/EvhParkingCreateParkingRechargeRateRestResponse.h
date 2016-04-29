//
// EvhParkingCreateParkingRechargeRateRestResponse.h
// generated at 2016-04-29 18:56:04 
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
