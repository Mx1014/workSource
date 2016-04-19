//
// EvhSearchByPMCommand.h
// generated at 2016-04-19 12:41:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchByPMCommand
//
@interface EvhSearchByPMCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* queryString;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* forumIds;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* communityIds;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

