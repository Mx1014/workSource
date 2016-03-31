//
// EvhVideoConfAccountPreferentialRuleDTO.h
// generated at 2016-03-28 15:56:09 
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

