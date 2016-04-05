//
// EvhListNearbyNeighborUserCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyNeighborUserCommand
//
@interface EvhListNearbyNeighborUserCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* pageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

