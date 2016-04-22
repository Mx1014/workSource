//
// EvhQueryTopicByEntityAndCategoryCommand.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryTopicByEntityAndCategoryCommand
//
@interface EvhQueryTopicByEntityAndCategoryCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSString* entityTag;

@property(nonatomic, copy) NSNumber* entityId;

@property(nonatomic, copy) NSString* targetTag;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

