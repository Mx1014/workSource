//
// EvhVideoConfAccountTrialRuleDTO.h
// generated at 2016-04-06 19:10:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVideoConfAccountTrialRuleDTO
//
@interface EvhVideoConfAccountTrialRuleDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* accounts;

@property(nonatomic, copy) NSNumber* months;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

