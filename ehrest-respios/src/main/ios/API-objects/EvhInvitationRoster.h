//
// EvhInvitationRoster.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitationRoster
//
@interface EvhInvitationRoster
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSNumber* inviteType;

@property(nonatomic, copy) NSString* userNickName;

@property(nonatomic, copy) NSNumber* regTime;

@property(nonatomic, copy) NSNumber* inviterId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

