//
// EvhOrganizationDTO.h
// generated at 2016-03-31 15:43:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDTO.h"

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

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

// item type EvhOrganizationDTO*
@property(nonatomic, strong) NSMutableArray* childrens;

@property(nonatomic, copy) NSNumber* roleId;

@property(nonatomic, copy) NSString* roleName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

