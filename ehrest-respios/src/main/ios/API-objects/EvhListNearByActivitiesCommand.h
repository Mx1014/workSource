//
// EvhListNearByActivitiesCommand.h
// generated at 2016-04-07 15:16:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNearByActivitiesCommand
//
@interface EvhListNearByActivitiesCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* longitude;

@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* anchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

