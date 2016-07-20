//
// EvhGetOwningFamilyByIdCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetOwningFamilyByIdCommand
//
@interface EvhGetOwningFamilyByIdCommand
    : EvhBaseCommand


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

