//
// EvhGetLaunchPadLayoutBySceneCommand.h
// generated at 2016-03-25 15:57:23 
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

