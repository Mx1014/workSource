//
// EvhAdminConfigurationListConfigurationsRestResponse.h
// generated at 2016-04-05 13:45:27 
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
