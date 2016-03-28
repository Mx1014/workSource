//
// EvhRecommendBannerInfo.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendBannerInfo
//
@interface EvhRecommendBannerInfo
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* redirectUrl;

@property(nonatomic, copy) NSString* posterUrl;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

