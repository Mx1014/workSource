//
// EvhUpdateCommunityCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCommunityCommand
//
@interface EvhUpdateCommunityCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSNumber* areaId;

// item type EvhCommunityGeoPointDTO*
@property(nonatomic, strong) NSMutableArray* geoPointList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

