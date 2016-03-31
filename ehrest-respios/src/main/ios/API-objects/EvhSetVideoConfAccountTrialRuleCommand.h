//
// EvhSetVideoConfAccountTrialRuleCommand.h
// generated at 2016-03-31 15:43:21 
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

