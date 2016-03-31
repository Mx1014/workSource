//
// EvhRecommendBannerListResponse.h
// generated at 2016-03-28 15:56:07 
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

