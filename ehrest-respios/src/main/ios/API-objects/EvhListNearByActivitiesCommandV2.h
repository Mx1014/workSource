//
// EvhListNearByActivitiesCommandV2.h
// generated at 2016-03-25 19:05:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearByActivitiesCommandV2
//
@interface EvhListNearByActivitiesCommandV2
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* community_id;

@property(nonatomic, copy) NSNumber* anchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

