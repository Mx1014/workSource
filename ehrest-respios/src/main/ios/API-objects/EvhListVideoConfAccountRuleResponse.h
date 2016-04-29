//
// EvhListVideoConfAccountRuleResponse.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:55 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

