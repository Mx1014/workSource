//
// EvhBuildingDTO.h
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

@property(nonatomic, copy) NSNumber* communityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

