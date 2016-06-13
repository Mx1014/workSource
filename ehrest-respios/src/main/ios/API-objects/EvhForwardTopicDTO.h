//
// EvhForwardTopicDTO.h
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

