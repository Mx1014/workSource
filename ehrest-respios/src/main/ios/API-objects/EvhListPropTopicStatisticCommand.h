//
// EvhListPropTopicStatisticCommand.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropTopicStatisticCommand
//
@interface EvhListPropTopicStatisticCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* categoryId;

@property(nonatomic, copy) NSString* startStrTime;

@property(nonatomic, copy) NSString* endStrTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

