//
// EvhFollowFamilyCommand.h
// generated at 2016-04-22 13:56:46 
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

