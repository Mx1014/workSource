//
// EvhTechparkParkListParkingChargeRestResponse.h
// generated at 2016-04-19 13:40:02 
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
