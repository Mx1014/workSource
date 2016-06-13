//
// EvhParkingCreateParkingRechargeRateRestResponse.h
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
