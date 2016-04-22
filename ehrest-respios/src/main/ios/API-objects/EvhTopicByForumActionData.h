//
// EvhTopicByForumActionData.h
// generated at 2016-04-22 13:56:48 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTopicByForumActionData
//
@interface EvhTopicByForumActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

