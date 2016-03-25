//
// EvhListUserRolesAdminCommandResponse.h
// generated at 2016-03-25 11:43:34 
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

