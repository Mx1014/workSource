//
// EvhListPmTopicStaticCommand.h
// generated at 2016-03-31 13:49:14 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmTopicStaticCommand
//
@interface EvhListPmTopicStaticCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

