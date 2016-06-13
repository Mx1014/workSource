//
// EvhQueryTopicByCategoryCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryTopicByCategoryCommand
//
@interface EvhQueryTopicByCategoryCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSString* entityTag;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* contentCategory;

@property(nonatomic, copy) NSNumber* actionCategory;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* embeddedAppId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* excludeCategories;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

