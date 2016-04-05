//
// EvhCreateInvitationCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateInvitationCommand
//
@interface EvhCreateInvitationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* inviteType;

@property(nonatomic, copy) NSString* targetEntityType;

@property(nonatomic, copy) NSNumber* targetEntityId;

@property(nonatomic, copy) NSString* identifiers;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* buildingNum;

@property(nonatomic, copy) NSString* aptNumber;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

