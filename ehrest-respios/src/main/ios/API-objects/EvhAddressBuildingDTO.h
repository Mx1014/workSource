//
// EvhAddressBuildingDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAddressBuildingDTO
//
@interface EvhAddressBuildingDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* buildingName;

@property(nonatomic, copy) NSString* buildingAliasName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

