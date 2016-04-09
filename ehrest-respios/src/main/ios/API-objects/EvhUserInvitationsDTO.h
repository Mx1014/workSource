//
// EvhUserInvitationsDTO.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInvitationsDTO
//
@interface EvhUserInvitationsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerUid;

@property(nonatomic, copy) NSString* inviteCode;

@property(nonatomic, copy) NSNumber* inviteType;

@property(nonatomic, copy) NSNumber* expiration;

@property(nonatomic, copy) NSString* targetEntityType;

@property(nonatomic, copy) NSNumber* targetEntityId;

@property(nonatomic, copy) NSNumber* maxInviteCount;

@property(nonatomic, copy) NSNumber* currentInviteCount;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

