//
// EvhGetEntranceByPrivilegeCommand.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetEntranceByPrivilegeCommand
//
@interface EvhGetEntranceByPrivilegeCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* module;

@property(nonatomic, copy) NSString* sceneToken;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

