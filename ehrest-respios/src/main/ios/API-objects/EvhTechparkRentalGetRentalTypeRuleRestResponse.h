//
// EvhTechparkRentalGetRentalTypeRuleRestResponse.h
// generated at 2016-03-31 13:49:15 
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
