//
// EvhUpdateCommunityAdminCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityGeoPointDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdateCommunityAdminCommand
//
@interface EvhUpdateCommunityAdminCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* cityId;

@property(nonatomic, copy) NSNumber* areaId;

@property(nonatomic, copy) NSNumber* areaSize;

// item type EvhCommunityGeoPointDTO*
@property(nonatomic, strong) NSMutableArray* geoPointList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

