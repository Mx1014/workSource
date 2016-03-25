//
// EvhAdminRecommendListConfigRestResponse.h
// generated at 2016-03-25 17:08:12 
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
