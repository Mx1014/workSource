//
// EvhCommunityUserDto.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCommunityUserDto
//
@interface EvhCommunityUserDto
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* isAuth;

@property(nonatomic, copy) NSString* enterpriseName;

@property(nonatomic, copy) NSString* buildingId;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSNumber* addressId;

@property(nonatomic, copy) NSString* addressName;

@property(nonatomic, copy) NSNumber* applyTime;

@property(nonatomic, copy) NSString* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

