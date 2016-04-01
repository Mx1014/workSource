//
// EvhSetAclRoleAssignmentCommand.h
// generated at 2016-04-01 15:40:23 
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

