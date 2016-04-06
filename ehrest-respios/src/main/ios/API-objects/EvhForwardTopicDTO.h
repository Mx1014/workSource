//
// EvhForwardTopicDTO.h
// generated at 2016-04-06 19:10:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhForwardTopicDTO
//
@interface EvhForwardTopicDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSString* subject;

@property(nonatomic, copy) NSString* contentUri;

@property(nonatomic, copy) NSString* contentUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

