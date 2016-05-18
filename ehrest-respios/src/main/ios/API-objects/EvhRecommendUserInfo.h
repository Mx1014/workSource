//
// EvhRecommendUserInfo.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRecommendUserInfo
//
@interface EvhRecommendUserInfo
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSNumber* userSourceType;

@property(nonatomic, copy) NSString* floorRelation;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

