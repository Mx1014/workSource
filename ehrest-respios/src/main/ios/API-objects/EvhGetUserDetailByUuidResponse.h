//
// EvhGetUserDetailByUuidResponse.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUserServiceAddressDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserDetailByUuidResponse
//
@interface EvhGetUserDetailByUuidResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSNumber* gender;

@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, strong) EvhUserServiceAddressDTO* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

