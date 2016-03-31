//
// EvhAdminRecommendListConfigRestResponse.h
// generated at 2016-03-28 15:56:09 
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
