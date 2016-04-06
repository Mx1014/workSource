//
// EvhListCommunityUsersCommand.h
// generated at 2016-04-06 19:59:46 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunityUsersCommand
//
@interface EvhListCommunityUsersCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* isAuth;

@property(nonatomic, copy) NSString* keywords;

@property(nonatomic, copy) NSString* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

