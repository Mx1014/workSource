//
// EvhTechparkParkRefreshParkingSystemRestResponse.h
// generated at 2016-04-19 13:40:02 
//
#import "RestResponseBase.h"
#import "EvhRechargeSuccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkRefreshParkingSystemRestResponse
//
@interface EvhTechparkParkRefreshParkingSystemRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRechargeSuccessResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
