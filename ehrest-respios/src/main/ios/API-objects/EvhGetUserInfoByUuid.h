//
// EvhGetUserInfoByUuid.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetUserInfoByUuid
//
@interface EvhGetUserInfoByUuid
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* uuid;

@property(nonatomic, copy) NSNumber* timestamp;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

