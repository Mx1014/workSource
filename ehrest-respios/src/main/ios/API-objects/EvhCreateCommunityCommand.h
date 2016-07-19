//
// EvhCreateCommunityCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateCommunityCommand
//
@interface EvhCreateCommunityCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* aliasName;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* communityType;

@property(nonatomic, copy) NSNumber* provinceId;

@property(nonatomic, copy) NSString* provinceName;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSString* cityName;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* areaName;

@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

