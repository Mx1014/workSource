//
// EvhDeleteRolePrivilegeCommand.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteRolePrivilegeCommand
//
@interface EvhDeleteRolePrivilegeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSNumber* roleId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

