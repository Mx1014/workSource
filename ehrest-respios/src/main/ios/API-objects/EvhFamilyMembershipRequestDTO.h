//
// EvhFamilyMembershipRequestDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyMembershipRequestDTO
//
@interface EvhFamilyMembershipRequestDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSString* familyName;

@property(nonatomic, copy) NSString* familyAvatarUri;

@property(nonatomic, copy) NSString* familyAvatarUrl;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* requestorUid;

@property(nonatomic, copy) NSString* requestorName;

@property(nonatomic, copy) NSString* requestorAvatar;

@property(nonatomic, copy) NSString* requestingTime;

@property(nonatomic, copy) NSString* requestorComment;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

