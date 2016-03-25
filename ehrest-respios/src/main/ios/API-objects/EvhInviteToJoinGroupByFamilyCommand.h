//
// EvhInviteToJoinGroupByFamilyCommand.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInviteToJoinGroupByFamilyCommand
//
@interface EvhInviteToJoinGroupByFamilyCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* groupId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* familyIds;

@property(nonatomic, copy) NSString* invitationText;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

