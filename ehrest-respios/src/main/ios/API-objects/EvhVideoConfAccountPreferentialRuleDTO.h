//
// EvhVideoConfAccountPreferentialRuleDTO.h
// generated at 2016-04-18 14:48:52 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountPreferentialRuleDTO
//
@interface EvhVideoConfAccountPreferentialRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* limit;

@property(nonatomic, copy) NSNumber* subtract;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

