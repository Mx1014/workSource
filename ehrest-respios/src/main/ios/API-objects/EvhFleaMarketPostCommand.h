//
// EvhFleaMarketPostCommand.h
// generated at 2016-03-25 17:08:10 
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

