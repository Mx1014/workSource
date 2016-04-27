//
// EvhAdminRecommendListConfigRestResponse.h
// generated at 2016-04-26 18:22:56 
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
