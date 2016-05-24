//
// EvhEnterpriseDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhAddressAddressDTO.h"
#import "EvhEnterpriseAttachmentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEnterpriseDTO
//
@interface EvhEnterpriseDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* contactCount;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* owningForumId;

@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* contactOf;

@property(nonatomic, copy) NSNumber* contactStatus;

@property(nonatomic, copy) NSString* contactNickName;

@property(nonatomic, copy) NSNumber* contactRole;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSString* creatorName;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* areaName;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* contactGroupPrivileges;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* contactForumPrivileges;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* contactsPhone;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSString* entries;

@property(nonatomic, copy) NSString* enterpriseCheckinDate;

@property(nonatomic, copy) NSString* enterpriseAddress;

@property(nonatomic, copy) NSString* postUri;

@property(nonatomic, copy) NSString* postUrl;

@property(nonatomic, copy) NSNumber* communityType;

@property(nonatomic, copy) NSNumber* defaultForumId;

@property(nonatomic, copy) NSNumber* feedbackForumId;

// item type EvhAddressAddressDTO*
@property(nonatomic, strong) NSMutableArray* address;

// item type EvhEnterpriseAttachmentDTO*
@property(nonatomic, strong) NSMutableArray* attachments;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

