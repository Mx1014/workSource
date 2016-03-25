//
// EvhAppInfo.h
// generated at 2016-03-25 17:08:10 
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

