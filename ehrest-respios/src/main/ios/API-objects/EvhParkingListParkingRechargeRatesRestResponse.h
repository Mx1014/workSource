//
// EvhParkingListParkingRechargeRatesRestResponse.h
// generated at 2016-04-07 10:47:33 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhParkingListParkingRechargeRatesRestResponse
//
@interface EvhParkingListParkingRechargeRatesRestResponse : EvhRestResponseBase

// array of EvhParkingRechargeRateDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
