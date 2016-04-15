//
// EvhTechparkParkRechargeResultRestResponse.h
// generated at 2016-04-12 15:02:21 
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
