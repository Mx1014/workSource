//
// EvhRecommendRecommendBannersRestResponse.h
// generated at 2016-03-25 19:05:21 
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
