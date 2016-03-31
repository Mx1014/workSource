//
// EvhListVideoConfAccountRuleResponse.h
// generated at 2016-03-28 15:56:07 
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

