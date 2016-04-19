//
// EvhListTopicCommand.h
// generated at 2016-04-19 14:25:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTopicCommand
//
@interface EvhListTopicCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* visibilityScope;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

