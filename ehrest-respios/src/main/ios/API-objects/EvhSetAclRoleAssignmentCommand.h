//
// EvhSetAclRoleAssignmentCommand.h
// generated at 2016-03-25 17:08:11 
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

