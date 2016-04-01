//
// EvhTechparkParkSetParkingPreferentialRuleRestResponse.h
// generated at 2016-03-31 20:15:34 
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
