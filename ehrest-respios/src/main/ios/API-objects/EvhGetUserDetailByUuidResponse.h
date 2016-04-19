//
// EvhGetUserDetailByUuidResponse.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
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

