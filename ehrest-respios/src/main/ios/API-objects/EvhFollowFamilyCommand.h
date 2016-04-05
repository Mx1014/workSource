//
// EvhFollowFamilyCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFollowFamilyCommand
//
@interface EvhFollowFamilyCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSString* aliasName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

