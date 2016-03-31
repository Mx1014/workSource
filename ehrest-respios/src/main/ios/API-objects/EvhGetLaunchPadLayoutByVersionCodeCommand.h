//
// EvhGetLaunchPadLayoutByVersionCodeCommand.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadLayoutByVersionCodeCommand
//
@interface EvhGetLaunchPadLayoutByVersionCodeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* versionCode;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSString* sceneType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

