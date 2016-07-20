//
// EvhCreateDoorAccessLingLing.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreateDoorAccessLingLing
//
@interface EvhCreateDoorAccessLingLing
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSNumber* existsId;

@property(nonatomic, copy) NSNumber* doorGroupId;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* address;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

