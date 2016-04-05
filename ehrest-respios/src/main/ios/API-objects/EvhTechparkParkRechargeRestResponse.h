//
// EvhTechparkParkRechargeRestResponse.h
// generated at 2016-04-05 13:45:27 
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
