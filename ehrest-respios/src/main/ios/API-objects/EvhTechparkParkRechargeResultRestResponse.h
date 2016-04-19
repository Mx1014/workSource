//
// EvhTechparkParkRechargeResultRestResponse.h
// generated at 2016-04-19 12:41:55 
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
