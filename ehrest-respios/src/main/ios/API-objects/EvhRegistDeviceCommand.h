//
// EvhRegistDeviceCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegistDeviceCommand
//
@interface EvhRegistDeviceCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* deviceId;

@property(nonatomic, copy) NSString* platform;

@property(nonatomic, copy) NSString* product;

@property(nonatomic, copy) NSString* brand;

@property(nonatomic, copy) NSString* deviceModel;

@property(nonatomic, copy) NSString* systemVersion;

@property(nonatomic, copy) NSString* meta;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

