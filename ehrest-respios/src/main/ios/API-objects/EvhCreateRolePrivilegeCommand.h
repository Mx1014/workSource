//
// EvhCreateRolePrivilegeCommand.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateRolePrivilegeCommand
//
@interface EvhCreateRolePrivilegeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* roleName;

@property(nonatomic, copy) NSString* description_;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* privilegeIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

