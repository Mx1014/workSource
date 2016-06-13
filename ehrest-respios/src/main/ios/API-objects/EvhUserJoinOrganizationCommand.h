//
// EvhUserJoinOrganizationCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserJoinOrganizationCommand
//
@interface EvhUserJoinOrganizationCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* orgType;

@property(nonatomic, copy) NSNumber* parentId;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* userJoin;

@property(nonatomic, copy) NSString* memberType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

