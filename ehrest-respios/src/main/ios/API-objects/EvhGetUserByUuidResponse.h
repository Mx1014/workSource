//
// EvhGetUserByUuidResponse.h
// generated at 2016-04-07 17:03:17 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserByUuidResponse
//
@interface EvhGetUserByUuidResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* nickName;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* avatarUrl;

@property(nonatomic, copy) NSNumber* gender;

@property(nonatomic, copy) NSString* uuid;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

