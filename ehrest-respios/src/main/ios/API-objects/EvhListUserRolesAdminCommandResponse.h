//
// EvhListUserRolesAdminCommandResponse.h
// generated at 2016-04-07 17:57:41 
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

