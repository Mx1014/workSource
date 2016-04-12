//
// EvhRecommendRecommendBannersRestResponse.h
// generated at 2016-04-08 20:09:24 
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
