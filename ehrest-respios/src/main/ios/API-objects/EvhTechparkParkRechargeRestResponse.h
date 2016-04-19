//
// EvhTechparkParkRechargeRestResponse.h
// generated at 2016-04-19 14:25:58 
//
#import "RestResponseBase.h"
#import "EvhRechargeOrderDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkRechargeRestResponse
//
@interface EvhTechparkParkRechargeRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRechargeOrderDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
