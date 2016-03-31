//
// EvhListNearbyActivitiesResponse.h
// generated at 2016-03-31 15:43:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearbyActivitiesResponse
//
@interface EvhListNearbyActivitiesResponse
    : NSObject<EvhJsonSerializable>


// item type EvhActivityDTO*
@property(nonatomic, strong) NSMutableArray* activities;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

