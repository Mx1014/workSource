//
// EvhAdminConfigurationListConfigurationsRestResponse.h
// generated at 2016-03-31 10:18:21 
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
