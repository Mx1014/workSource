//
// EvhBuildingDTO.h
// generated at 2016-04-07 17:57:41 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBuildingDTO
//
@interface EvhBuildingDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* buildingAliasName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

