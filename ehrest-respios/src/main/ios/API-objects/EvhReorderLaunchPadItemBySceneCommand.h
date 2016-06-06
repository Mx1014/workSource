//
// EvhReorderLaunchPadItemBySceneCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhLaunchPadItemSort.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReorderLaunchPadItemBySceneCommand
//
@interface EvhReorderLaunchPadItemBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

// item type EvhLaunchPadItemSort*
@property(nonatomic, strong) NSMutableArray* sorts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

