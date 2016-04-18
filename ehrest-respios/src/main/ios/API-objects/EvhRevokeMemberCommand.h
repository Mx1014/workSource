//
// EvhRevokeMemberCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRevokeMemberCommand
//
@interface EvhRevokeMemberCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* memberUid;

@property(nonatomic, copy) NSString* reason;

@property(nonatomic, copy) NSNumber* operatorRole;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

