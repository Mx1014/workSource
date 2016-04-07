//
// EvhSetAclRoleAssignmentCommand.h
// generated at 2016-04-07 14:16:31 
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

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

