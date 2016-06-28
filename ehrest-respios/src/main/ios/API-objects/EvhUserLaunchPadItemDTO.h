//
// EvhUserLaunchPadItemDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUserLaunchPadItemDTO
//
@interface EvhUserLaunchPadItemDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* itemId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* applyPolicy;

@property(nonatomic, copy) NSNumber* displayFlag;

@property(nonatomic, copy) NSNumber* defaultOrder;

@property(nonatomic, copy) NSString* sceneType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

