//
// EvhUpdateRolePrivilegeCommand.h
// generated at 2016-03-25 17:08:11 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateRolePrivilegeCommand
//
@interface EvhUpdateRolePrivilegeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* roleId;

@property(nonatomic, copy) NSString* roleName;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* privilegeIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

