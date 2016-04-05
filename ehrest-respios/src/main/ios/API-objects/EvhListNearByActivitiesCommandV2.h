//
// EvhListNearByActivitiesCommandV2.h
// generated at 2016-04-05 13:45:25 
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

