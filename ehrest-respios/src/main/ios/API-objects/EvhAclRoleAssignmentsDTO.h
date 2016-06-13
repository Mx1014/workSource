//
// EvhAclRoleAssignmentsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclRoleAssignmentsDTO
//
@interface EvhAclRoleAssignmentsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* targetType;

@property(nonatomic, copy) NSNumber* targetId;

@property(nonatomic, copy) NSString* targetName;

@property(nonatomic, copy) NSNumber* roleId;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* roleName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

