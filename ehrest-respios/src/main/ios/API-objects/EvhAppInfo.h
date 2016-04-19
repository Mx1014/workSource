//
// EvhAppInfo.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAppInfo
//
@interface EvhAppInfo
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appName;

@property(nonatomic, copy) NSString* appVersion;

@property(nonatomic, copy) NSString* appSize;

@property(nonatomic, copy) NSString* appInstalledTime;

@property(nonatomic, copy) NSNumber* collectTimeMs;

@property(nonatomic, copy) NSNumber* reportTimeMs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

