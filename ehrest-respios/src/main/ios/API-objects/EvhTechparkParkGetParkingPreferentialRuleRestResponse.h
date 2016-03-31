//
// EvhTechparkParkGetParkingPreferentialRuleRestResponse.h
// generated at 2016-03-31 13:49:15 
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
