//
// EvhListActivitiesByTagCommand.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListActivitiesByTagCommand
//
@interface EvhListActivitiesByTagCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* tag;

@property(nonatomic, copy) NSNumber* community_id;

@property(nonatomic, copy) NSNumber* anchor;

@property(nonatomic, copy) NSNumber* range;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

