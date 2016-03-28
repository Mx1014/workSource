//
// EvhPmBuildingDTO.h
// generated at 2016-03-25 19:05:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBuildingDTO
//
@interface EvhPmBuildingDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* buildingId;

@property(nonatomic, copy) NSString* buildingName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

