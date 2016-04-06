//
// EvhAdminRecommendListConfigRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"
#import "EvhListRecommendConfigResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminRecommendListConfigRestResponse
//
@interface EvhAdminRecommendListConfigRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListRecommendConfigResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
