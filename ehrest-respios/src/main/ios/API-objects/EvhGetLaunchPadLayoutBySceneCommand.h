//
// EvhGetLaunchPadLayoutBySceneCommand.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadLayoutBySceneCommand
//
@interface EvhGetLaunchPadLayoutBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* versionCode;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

