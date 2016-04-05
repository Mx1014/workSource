//
// EvhSceneDTO.h
// generated at 2016-04-05 13:45:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSceneDTO
//
@interface EvhSceneDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* sceneToken;

@property(nonatomic, copy) NSString* entityType;

@property(nonatomic, copy) NSString* entityContent;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* avatar;

@property(nonatomic, copy) NSString* avatarUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

