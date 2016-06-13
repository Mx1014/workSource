//
// EvhTechparkParkListParkingChargeRestResponse.h
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
