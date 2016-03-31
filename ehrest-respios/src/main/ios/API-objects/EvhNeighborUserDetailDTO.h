//
// EvhNeighborUserDetailDTO.h
// generated at 2016-03-31 11:07:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhNeighborUserDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNeighborUserDetailDTO
//
@interface EvhNeighborUserDetailDTO
    : EvhNeighborUserDTO


@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* apartmentFloor;

@property(nonatomic, copy) NSString* initial;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

