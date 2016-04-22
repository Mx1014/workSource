//
// EvhListNearbyActivitiesResponse.h
// generated at 2016-04-22 13:56:48 
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

