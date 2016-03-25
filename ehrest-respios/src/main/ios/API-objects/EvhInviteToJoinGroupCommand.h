//
// EvhInviteToJoinGroupCommand.h
// generated at 2016-03-25 17:08:12 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToJoinGroupCommand
//
@interface EvhInviteToJoinGroupCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* userIds;

@property(nonatomic, copy) NSString* invitationText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

