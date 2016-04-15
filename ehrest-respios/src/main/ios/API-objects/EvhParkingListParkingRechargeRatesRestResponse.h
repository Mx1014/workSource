//
// EvhParkingListParkingRechargeRatesRestResponse.h
// generated at 2016-04-12 15:02:21 
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
