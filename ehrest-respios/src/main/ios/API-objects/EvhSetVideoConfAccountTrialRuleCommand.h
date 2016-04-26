//
// EvhSetVideoConfAccountTrialRuleCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetVideoConfAccountTrialRuleCommand
//
@interface EvhSetVideoConfAccountTrialRuleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* accounts;

@property(nonatomic, copy) NSString* months;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

