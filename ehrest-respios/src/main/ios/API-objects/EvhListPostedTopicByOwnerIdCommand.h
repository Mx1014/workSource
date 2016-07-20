//
// EvhListPostedTopicByOwnerIdCommand.h
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

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* excludeCategories;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

