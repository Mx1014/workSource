//
// EvhTechparkRentalGetRentalTypeRuleRestResponse.h
// generated at 2016-04-07 17:57:44 
//
#import "RestResponseBase.h"
#import "EvhGetRentalTypeRuleCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalGetRentalTypeRuleRestResponse
//
@interface EvhTechparkRentalGetRentalTypeRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhGetRentalTypeRuleCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
