//
// EvhPmManagementCommunityDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmManagementCommunityDTO
//
@interface EvhPmManagementCommunityDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* aliasName;

@property(nonatomic, copy) NSNumber* aptCount;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* communityType;

@property(nonatomic, copy) NSNumber* areaSize;

@property(nonatomic, copy) NSNumber* isAll;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

