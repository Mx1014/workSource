//
// EvhBriefNewsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBriefNewsDTO
//
@interface EvhBriefNewsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* theNewsToken;

@property(nonatomic, copy) NSString* title;

@property(nonatomic, copy) NSNumber* releaseTime;

@property(nonatomic, copy) NSString* author;

@property(nonatomic, copy) NSString* sourceDesc;

@property(nonatomic, copy) NSString* coverUri;

@property(nonatomic, copy) NSString* contentAbstract;

@property(nonatomic, copy) NSNumber* likeCount;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSNumber* topFlag;

@property(nonatomic, copy) NSNumber* likeFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

