//
// EvhOwnerDoorDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOwnerDoorDTO
//
@interface EvhOwnerDoorDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* doorId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

