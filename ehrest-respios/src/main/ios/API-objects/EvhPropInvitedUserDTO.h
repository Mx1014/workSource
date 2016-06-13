//
// EvhPropInvitedUserDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropInvitedUserDTO
//
@interface EvhPropInvitedUserDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSNumber* inviteType;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSNumber* registerTime;

@property(nonatomic, copy) NSNumber* invitorId;

@property(nonatomic, copy) NSString* invitorName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

