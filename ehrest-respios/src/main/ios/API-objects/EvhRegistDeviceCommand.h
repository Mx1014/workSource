//
// EvhRegistDeviceCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
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

