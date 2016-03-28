//
// EvhTechparkRentalGetRentalTypeRuleRestResponse.h
// generated at 2016-03-25 19:05:21 
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
