//
// EvhFavoriteBusinessesBySceneCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhFavoriteBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFavoriteBusinessesBySceneCommand
//
@interface EvhFavoriteBusinessesBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

// item type EvhFavoriteBusinessDTO*
@property(nonatomic, strong) NSMutableArray* bizs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

