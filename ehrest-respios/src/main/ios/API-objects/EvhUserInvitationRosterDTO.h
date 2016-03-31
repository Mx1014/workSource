//
// EvhUserInvitationRosterDTO.h
// generated at 2016-03-31 13:49:13 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserInvitationRosterDTO
//
@interface EvhUserInvitationRosterDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* inviteId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* contact;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

