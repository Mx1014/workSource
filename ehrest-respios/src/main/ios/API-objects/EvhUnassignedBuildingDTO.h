//
// EvhUnassignedBuildingDTO.h
// generated at 2016-04-08 20:09:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUnassignedBuildingDTO
//
@interface EvhUnassignedBuildingDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* buildingName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

