//
// EvhListVideoConfAccountRuleResponse.h
// generated at 2016-04-01 15:40:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhVideoConfAccountRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountRuleResponse
//
@interface EvhListVideoConfAccountRuleResponse
    : NSObject<EvhJsonSerializable>


// item type EvhVideoConfAccountRuleDTO*
@property(nonatomic, strong) NSMutableArray* rules;

@property(nonatomic, copy) NSNumber* nextPageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

