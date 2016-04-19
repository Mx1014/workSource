//
// EvhListActivitiesByTagCommand.h
// generated at 2016-04-19 14:25:56 
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

