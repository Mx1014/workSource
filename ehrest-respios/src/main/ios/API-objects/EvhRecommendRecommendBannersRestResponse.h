//
// EvhRecommendRecommendBannersRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhRecommendBannerListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendRecommendBannersRestResponse
//
@interface EvhRecommendRecommendBannersRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhRecommendBannerListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
