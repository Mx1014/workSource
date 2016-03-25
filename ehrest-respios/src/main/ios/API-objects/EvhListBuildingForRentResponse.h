//
// EvhListBuildingForRentResponse.h
// generated at 2016-03-25 17:08:12 
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

