//
// EvhCreateDoorAccessGroup.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDoorAccessGroup
//
@interface EvhCreateDoorAccessGroup
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* groupType;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

