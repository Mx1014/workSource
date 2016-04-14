//
// EvhInvitationDTO.h
// generated at 2016-04-12 19:00:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInvitationDTO
//
@interface EvhInvitationDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* identifier;

@property(nonatomic, copy) NSNumber* inviteType;

@property(nonatomic, copy) NSString* inviteName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

