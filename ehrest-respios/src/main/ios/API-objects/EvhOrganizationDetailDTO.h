//
// EvhOrganizationDetailDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationMemberDTO.h"
#import "EvhCommunityDTO.h"
#import "EvhAddressAddressDTO.h"
#import "EvhForumAttachmentDescriptor.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOrganizationDetailDTO
//
@interface EvhOrganizationDetailDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* contact;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* geohash;

@property(nonatomic, copy) NSString* displayName;

@property(nonatomic, copy) NSString* contactor;

@property(nonatomic, copy) NSNumber* memberCount;

@property(nonatomic, copy) NSNumber* checkinDate;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* avatarUri;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSNumber* updateTime;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* accountPhone;

@property(nonatomic, copy) NSString* accountName;

@property(nonatomic, copy) NSNumber* assignmentId;

@property(nonatomic, copy) NSString* postUri;

@property(nonatomic, copy) NSString* postUrl;

@property(nonatomic, copy) NSString* longitude;

@property(nonatomic, copy) NSString* latitude;

@property(nonatomic, strong) EvhOrganizationMemberDTO* member;

@property(nonatomic, strong) EvhCommunityDTO* community;

// item type EvhAddressAddressDTO*
@property(nonatomic, strong) NSMutableArray* addresses;

// item type EvhForumAttachmentDescriptor*
@property(nonatomic, strong) NSMutableArray* attachments;

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

