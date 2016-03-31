//
// EvhTechparkParkRechargeRestResponse.h
// generated at 2016-03-31 19:08:54 
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
