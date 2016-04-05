//
// EvhTechparkParkRechargeResultRestResponse.h
// generated at 2016-04-05 13:45:27 
//
#import "RestResponseBase.h"
#import "EvhRechargeSuccessResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkRechargeResultRestResponse
//
@interface EvhTechparkParkRechargeResultRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRechargeSuccessResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
