//
// EvhListConfAccountSaleRuleCommand.h
// generated at 2016-04-26 18:22:54 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListConfAccountSaleRuleCommand
//
@interface EvhListConfAccountSaleRuleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* confType;

@property(nonatomic, copy) NSNumber* isOnline;

@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

