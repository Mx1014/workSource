//
// EvhVideoConfAccountPreferentialRuleDTO.h
// generated at 2016-03-31 13:49:14 
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

