//
// EvhAdminConfigurationListConfigurationsRestResponse.h
// generated at 2016-04-12 19:00:53 
//
#import "RestResponseBase.h"
#import "EvhListConfigurationsAdminCommandResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminConfigurationListConfigurationsRestResponse
//
@interface EvhAdminConfigurationListConfigurationsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListConfigurationsAdminCommandResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
