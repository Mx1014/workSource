//
// EvhListOwningFamilyMembersCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListOwningFamilyMembersCommand
//
@interface EvhListOwningFamilyMembersCommand
    : EvhBaseCommand


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

