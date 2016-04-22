//
// EvhRejectCommunityAdminCommand.h
// generated at 2016-04-22 13:56:44 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRejectCommunityAdminCommand
//
@interface EvhRejectCommunityAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* operatorRole;

@property(nonatomic, copy) NSString* reason;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

