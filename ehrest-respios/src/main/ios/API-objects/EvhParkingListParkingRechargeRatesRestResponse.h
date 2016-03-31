//
// EvhParkingListParkingRechargeRatesRestResponse.h
// generated at 2016-03-31 10:18:21 
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
