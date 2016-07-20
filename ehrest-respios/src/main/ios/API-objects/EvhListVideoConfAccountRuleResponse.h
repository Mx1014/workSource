//
// EvhListVideoConfAccountRuleResponse.h
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

