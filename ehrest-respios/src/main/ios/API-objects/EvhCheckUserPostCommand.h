//
// EvhCheckUserPostCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCheckUserPostCommand
//
@interface EvhCheckUserPostCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSNumber* timestamp;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

