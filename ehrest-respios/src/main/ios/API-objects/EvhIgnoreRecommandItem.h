//
// EvhIgnoreRecommandItem.h
// generated at 2016-03-25 19:05:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhIgnoreRecommandItem
//
@interface EvhIgnoreRecommandItem
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* suggestType;

@property(nonatomic, copy) NSNumber* sourceId;

@property(nonatomic, copy) NSNumber* sourceType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

