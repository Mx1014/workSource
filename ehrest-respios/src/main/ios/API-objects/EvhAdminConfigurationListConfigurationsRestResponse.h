//
// EvhAdminConfigurationListConfigurationsRestResponse.h
// generated at 2016-04-18 14:48:52 
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
