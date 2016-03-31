//
// EvhGetLaunchPadItemsBySceneCommand.h
// generated at 2016-03-28 15:56:08 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetLaunchPadItemsBySceneCommand
//
@interface EvhGetLaunchPadItemsBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* itemLocation;

@property(nonatomic, copy) NSString* itemGroup;

@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

