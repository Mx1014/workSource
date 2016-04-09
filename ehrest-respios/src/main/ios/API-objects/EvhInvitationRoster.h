//
// EvhInvitationRoster.h
// generated at 2016-04-08 20:09:23 
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

