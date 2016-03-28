//
// EvhFleaMarketUpdateCommand.h
// generated at 2016-03-25 19:05:18 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFleaMarketUpdateCommand
//
@interface EvhFleaMarketUpdateCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* forumId;

@property(nonatomic, copy) NSNumber* topicId;

@property(nonatomic, copy) NSNumber* barterFlag;

@property(nonatomic, copy) NSString* price;

@property(nonatomic, copy) NSNumber* closeFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

