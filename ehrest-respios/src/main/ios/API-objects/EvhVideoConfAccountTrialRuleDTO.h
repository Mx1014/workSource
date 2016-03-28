//
// EvhVideoConfAccountTrialRuleDTO.h
// generated at 2016-03-25 19:05:20 
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

