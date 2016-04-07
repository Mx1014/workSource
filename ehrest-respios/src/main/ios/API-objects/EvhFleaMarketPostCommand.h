//
// EvhFleaMarketPostCommand.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhNewTopicCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFleaMarketPostCommand
//
@interface EvhFleaMarketPostCommand
    : EvhNewTopicCommand


@property(nonatomic, copy) NSNumber* barterFlag;

@property(nonatomic, copy) NSString* price;

@property(nonatomic, copy) NSNumber* closeFlag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

