//
// EvhSearchByMultiForumAndCmntyCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchByMultiForumAndCmntyCommand
//
@interface EvhSearchByMultiForumAndCmntyCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* queryString;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* forumIds;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* communityIds;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* regionIds;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

