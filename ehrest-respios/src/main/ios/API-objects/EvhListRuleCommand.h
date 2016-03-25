//
// EvhListRuleCommand.h
// generated at 2016-03-25 15:57:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListRuleCommand
//
@interface EvhListRuleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* channelType;

@property(nonatomic, copy) NSNumber* confType;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

