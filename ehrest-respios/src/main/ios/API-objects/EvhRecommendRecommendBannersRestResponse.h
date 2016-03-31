//
// EvhRecommendRecommendBannersRestResponse.h
// generated at 2016-03-31 19:08:54 
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
