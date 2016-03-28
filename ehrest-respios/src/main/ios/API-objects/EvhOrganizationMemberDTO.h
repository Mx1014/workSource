//
// EvhOrganizationMemberDTO.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAclRoleAssignmentsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationMemberDTO
//
@interface EvhOrganizationMemberDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* organizationName;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* memberGroup;

@property(nonatomic, copy) NSString* contactName;

@property(nonatomic, copy) NSNumber* contactType;

@property(nonatomic, copy) NSString* contactToken;

@property(nonatomic, copy) NSString* contactDescription;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSString* initial;

// item type EvhAclRoleAssignmentsDTO*
@property(nonatomic, strong) NSMutableArray* aclRoles;

@property(nonatomic, copy) NSNumber* groupId;

@property(nonatomic, copy) NSString* groupName;

@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* employeeNo;

@property(nonatomic, copy) NSNumber* gender;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

