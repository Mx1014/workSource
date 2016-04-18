//
// EvhOrganizationDTO.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDTO.h"
#import "EvhRoleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationDTO
//
@interface EvhOrganizationDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* parentName;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* path;

@property(nonatomic, copy) NSNumber* level;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* OrganizationType;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* memberStatus;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* groupType;

@property(nonatomic, copy) NSNumber* directlyEnterpriseId;

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

// item type EvhOrganizationDTO*
@property(nonatomic, strong) NSMutableArray* childrens;

// item type EvhRoleDTO*
@property(nonatomic, strong) NSMutableArray* roles;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

