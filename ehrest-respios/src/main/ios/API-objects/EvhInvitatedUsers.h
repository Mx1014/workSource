//
// EvhInvitatedUsers.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitatedUsers
//
@interface EvhInvitatedUsers
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* inviterId;

@property(nonatomic, copy) NSString* userNickName;

@property(nonatomic, copy) NSString* userCellPhone;

@property(nonatomic, copy) NSNumber* registerTime;

@property(nonatomic, copy) NSNumber* inviteType;

@property(nonatomic, copy) NSString* inviter;

@property(nonatomic, copy) NSString* inviterCellPhone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

