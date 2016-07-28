//
// EvhTechparkRentalGetRentalTypeRuleRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRentalGetRentalTypeRuleCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkRentalGetRentalTypeRuleRestResponse
//
@interface EvhTechparkRentalGetRentalTypeRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRentalGetRentalTypeRuleCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
