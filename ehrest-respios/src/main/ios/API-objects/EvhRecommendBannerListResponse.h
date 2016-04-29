//
// EvhRecommendBannerListResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

