//
// EvhListUserRolesAdminCommandResponse.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAclRoleAssignmentsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListUserRolesAdminCommandResponse
//
@interface EvhListUserRolesAdminCommandResponse
    : NSObject<EvhJsonSerializable>


// item type EvhAclRoleAssignmentsDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

