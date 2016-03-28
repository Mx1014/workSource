//
// EvhTechparkParkRefreshParkingSystemRestResponse.h
// generated at 2016-03-25 19:05:21 
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
