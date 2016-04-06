//
// EvhTechparkParkSetParkingPreferentialRuleRestResponse.h
// generated at 2016-04-06 19:10:44 
//
#import "RestResponseBase.h"
#import "EvhParkingPreferentialRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkParkSetParkingPreferentialRuleRestResponse
//
@interface EvhTechparkParkSetParkingPreferentialRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhParkingPreferentialRuleDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
