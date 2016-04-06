//
// EvhTechparkParkGetParkingPreferentialRuleRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhParkingPreferentialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkGetParkingPreferentialRuleRestResponse
//
@interface EvhTechparkParkGetParkingPreferentialRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingPreferentialRuleDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
