//
// EvhRecommendBannerListResponse.h
// generated at 2016-03-25 17:08:10 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRecommendBannerInfo.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendBannerListResponse
//
@interface EvhRecommendBannerListResponse
    : NSObject<EvhJsonSerializable>


// item type EvhRecommendBannerInfo*
@property(nonatomic, strong) NSMutableArray* banners;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

