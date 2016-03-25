//
// EvhListFollowersCommand.h
// generated at 2016-03-25 11:43:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaseCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFollowersCommand
//
@interface EvhListFollowersCommand
    : EvhBaseCommand


@property(nonatomic, copy) NSNumber* pageOffset;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

