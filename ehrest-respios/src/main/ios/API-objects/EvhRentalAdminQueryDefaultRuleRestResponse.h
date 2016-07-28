//
// EvhRentalAdminQueryDefaultRuleRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhQueryDefaultRuleAdminResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalAdminQueryDefaultRuleRestResponse
//
@interface EvhRentalAdminQueryDefaultRuleRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhQueryDefaultRuleAdminResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
