//
// EvhGetNewsDetailInfoResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNewsDetailInfoResponse
//
@interface EvhGetNewsDetailInfoResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* theNewsToken;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSNumber* releaseTime;

@property(nonatomic, copy) NSString* author;

@property(nonatomic, copy) NSString* sourceDesc;

@property(nonatomic, copy) NSString* sourceUrl;

@property(nonatomic, copy) NSString* contentUrl;

@property(nonatomic, copy) NSString* content;

@property(nonatomic, copy) NSNumber* likeCount;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSNumber* viewCount;

@property(nonatomic, copy) NSNumber* likeFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

