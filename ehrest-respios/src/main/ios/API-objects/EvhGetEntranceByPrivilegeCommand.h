//
// EvhGetEntranceByPrivilegeCommand.h
// generated at 2016-04-01 15:40:23 
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

