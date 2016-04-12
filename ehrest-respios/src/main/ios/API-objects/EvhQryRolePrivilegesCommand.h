//
// EvhQryRolePrivilegesCommand.h
// generated at 2016-04-12 19:00:51 
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

