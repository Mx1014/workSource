//
// EvhRecommendRecommendBannersRestResponse.h
// generated at 2016-04-18 14:48:52 
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
