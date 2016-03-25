//
// EvhInviteToJoinGroupByPhoneCommand.h
// generated at 2016-03-25 09:26:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToJoinGroupByPhoneCommand
//
@interface EvhInviteToJoinGroupByPhoneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* phones;

@property(nonatomic, copy) NSString* invitationText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

