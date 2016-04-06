//
// EvhParkingRequestParkingCardRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhParkingRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingRequestParkingCardRestResponse
//
@interface EvhParkingRequestParkingCardRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingRechargeOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
