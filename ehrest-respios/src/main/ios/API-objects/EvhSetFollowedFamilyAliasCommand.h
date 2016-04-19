//
// EvhSetFollowedFamilyAliasCommand.h
// generated at 2016-04-19 14:25:56 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetFollowedFamilyAliasCommand
//
@interface EvhSetFollowedFamilyAliasCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSString* aliasName;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

