//
// EvhSetAclRoleAssignmentCommand.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetAclRoleAssignmentCommand
//
@interface EvhSetAclRoleAssignmentCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSNumber* roleId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

