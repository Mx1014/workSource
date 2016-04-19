//
// EvhSetVideoConfAccountPreferentialRuleCommand.h
// generated at 2016-04-19 14:25:56 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetVideoConfAccountPreferentialRuleCommand
//
@interface EvhSetVideoConfAccountPreferentialRuleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* limit;

@property(nonatomic, copy) NSString* subtract;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

