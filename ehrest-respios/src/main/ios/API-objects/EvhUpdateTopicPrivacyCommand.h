//
// EvhUpdateTopicPrivacyCommand.h
// generated at 2016-04-06 19:10:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateTopicPrivacyCommand
//
@interface EvhUpdateTopicPrivacyCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* privacy;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

