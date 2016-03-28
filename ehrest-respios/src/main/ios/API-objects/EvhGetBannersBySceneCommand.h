//
// EvhGetBannersBySceneCommand.h
// generated at 2016-03-25 19:05:19 
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

