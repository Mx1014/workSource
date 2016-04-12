//
// EvhParkingCreateParkingRechargeRateRestResponse.h
// generated at 2016-04-12 15:02:21 
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
