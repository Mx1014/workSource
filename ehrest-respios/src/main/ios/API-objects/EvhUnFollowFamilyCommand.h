//
// EvhUnFollowFamilyCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUnFollowFamilyCommand
//
@interface EvhUnFollowFamilyCommand
    : EvhBaseCommand


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

