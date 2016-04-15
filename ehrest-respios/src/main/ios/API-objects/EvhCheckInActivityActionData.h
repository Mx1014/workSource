//
// EvhCheckInActivityActionData.h
// generated at 2016-04-12 15:02:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCheckInActivityActionData
//
@interface EvhCheckInActivityActionData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* activityId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

