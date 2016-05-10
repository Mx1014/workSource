//
// EvhQryRolePrivilegesCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQryRolePrivilegesCommand
//
@interface EvhQryRolePrivilegesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* roleId;

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

