//
// EvhAppInfo.h
// generated at 2016-04-26 18:22:54 
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

