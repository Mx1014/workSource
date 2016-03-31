//
// EvhAdminRecommendListConfigRestResponse.h
// generated at 2016-03-31 11:07:27 
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
