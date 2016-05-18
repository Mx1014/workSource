//
// EvhSearchTopicAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTopicAdminCommand
//
@interface EvhSearchTopicAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* senderPhones;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* senderNickNames;

@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

