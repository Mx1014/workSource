//
// EvhSetVideoConfAccountPreferentialRuleCommand.h
// generated at 2016-04-07 15:16:53 
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

