//
// EvhListNeighborUsersCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNeighborUsersCommand
//
@interface EvhListNeighborUsersCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* pageOffset;

@property(nonatomic, copy) NSNumber* isPinyin;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

