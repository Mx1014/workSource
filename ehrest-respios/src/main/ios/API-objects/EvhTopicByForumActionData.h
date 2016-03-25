//
// EvhTopicByForumActionData.h
// generated at 2016-03-25 11:43:33 
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

