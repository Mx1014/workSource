//
// EvhTopicScopeDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTopicScopeDTO
//
@interface EvhTopicScopeDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSString* targetTag;

@property(nonatomic, copy) NSNumber* leafFlag;

@property(nonatomic, copy) NSNumber* defaultFlag;

@property(nonatomic, copy) NSNumber* visibleRegionId;

@property(nonatomic, copy) NSNumber* visibleRegionType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

