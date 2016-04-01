//
// EvhTechparkParkListParkingChargeRestResponse.h
// generated at 2016-04-01 15:40:24 
//
#import "RestResponseBase.h"
#import "EvhParkResponseList.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkListParkingChargeRestResponse
//
@interface EvhTechparkParkListParkingChargeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkResponseList* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
