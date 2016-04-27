//
// EvhListBuildingForRentResponse.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBuildingForRentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListBuildingForRentResponse
//
@interface EvhListBuildingForRentResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhBuildingForRentDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

