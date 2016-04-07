//
// EvhSyncBusinessCommand.h
// generated at 2016-04-07 14:16:30 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBusinessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncBusinessCommand
//
@interface EvhSyncBusinessCommand
    : EvhBusinessCommand


@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

