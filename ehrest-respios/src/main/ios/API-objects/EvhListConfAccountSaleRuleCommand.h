//
// EvhListConfAccountSaleRuleCommand.h
// generated at 2016-04-05 13:45:26 
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

