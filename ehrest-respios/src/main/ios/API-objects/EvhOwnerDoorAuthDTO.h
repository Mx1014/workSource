//
// EvhOwnerDoorAuthDTO.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOwnerDoorAuthDTO
//
@interface EvhOwnerDoorAuthDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSNumber* doorAuthId;

@property(nonatomic, copy) NSNumber* ownerType;

@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

