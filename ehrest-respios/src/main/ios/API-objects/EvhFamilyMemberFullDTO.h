//
// EvhFamilyMemberFullDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFamilyMemberFullDTO
//
@interface EvhFamilyMemberFullDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSString* familyName;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* communityName;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSNumber* membershipStatus;

@property(nonatomic, copy) NSNumber* memberUid;

@property(nonatomic, copy) NSString* memberNickName;

@property(nonatomic, copy) NSString* cellPhone;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentName;

@property(nonatomic, copy) NSNumber* addressStatus;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

