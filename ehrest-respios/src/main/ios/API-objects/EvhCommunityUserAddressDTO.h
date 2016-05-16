//
// EvhCommunityUserAddressDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhOrganizationDetailDTO.h"
#import "EvhAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserAddressDTO
//
@interface EvhCommunityUserAddressDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* nikeName;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* isAuth;

@property(nonatomic, copy) NSNumber* applyTime;

@property(nonatomic, copy) NSString* phone;

@property(nonatomic, copy) NSNumber* gender;

// item type EvhOrganizationDetailDTO*
@property(nonatomic, strong) NSMutableArray* orgDtos;

// item type EvhAddressDTO*
@property(nonatomic, strong) NSMutableArray* addressDtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

