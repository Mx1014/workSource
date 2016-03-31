//
// EvhTechparkParkListParkingChargeRestResponse.h
// generated at 2016-03-31 15:43:24 
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
