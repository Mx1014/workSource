//
// EvhSetPrimaryFamilyCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetPrimaryFamilyCommand
//
@interface EvhSetPrimaryFamilyCommand
    : EvhBaseCommand


-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

