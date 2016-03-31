//
// EvhGetEntranceByPrivilegeCommand.h
// generated at 2016-03-31 11:07:26 
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

