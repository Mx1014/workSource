//
// EvhCreateInvitationCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

