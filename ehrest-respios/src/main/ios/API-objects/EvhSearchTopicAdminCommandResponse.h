//
// EvhSearchTopicAdminCommandResponse.h
// generated at 2016-04-19 13:39:59 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPostAdminDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTopicAdminCommandResponse
//
@interface EvhSearchTopicAdminCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPostAdminDTO*
@property(nonatomic, strong) NSMutableArray* posts;

@property(nonatomic, copy) NSString* keywords;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

