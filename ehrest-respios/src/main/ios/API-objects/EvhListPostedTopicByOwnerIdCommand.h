//
// EvhListPostedTopicByOwnerIdCommand.h
// generated at 2016-04-19 12:41:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPostedTopicByOwnerIdCommand
//
@interface EvhListPostedTopicByOwnerIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerUid;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

