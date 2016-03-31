//
// EvhGetPunchRuleCommandResponse.h
// generated at 2016-03-31 11:07:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPunchRuleDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetPunchRuleCommandResponse
//
@interface EvhGetPunchRuleCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhPunchRuleDTO* punchRuleDTO;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

