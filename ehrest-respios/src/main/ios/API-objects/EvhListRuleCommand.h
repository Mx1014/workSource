//
// EvhListRuleCommand.h
// generated at 2016-03-25 09:26:39 
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

