//
// EvhTechparkRentalGetRentalTypeRuleRestResponse.h
// generated at 2016-04-08 20:09:24 
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
