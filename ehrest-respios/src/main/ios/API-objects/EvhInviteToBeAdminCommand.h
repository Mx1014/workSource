//
// EvhInviteToBeAdminCommand.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToBeAdminCommand
//
@interface EvhInviteToBeAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* invitationText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

