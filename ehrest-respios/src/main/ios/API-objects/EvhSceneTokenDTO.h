//
// EvhSceneTokenDTO.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSceneTokenDTO
//
@interface EvhSceneTokenDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* scene;

@property(nonatomic, copy) NSString* entityType;

@property(nonatomic, copy) NSNumber* entityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

