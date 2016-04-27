//
// EvhGetBannersBySceneCommand.h
// generated at 2016-04-26 18:22:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBannersBySceneCommand
//
@interface EvhGetBannersBySceneCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* bannerLocation;

@property(nonatomic, copy) NSString* bannerGroup;

@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

