//
// EvhReSyncBusinessCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBusinessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhReSyncBusinessCommand
//
@interface EvhReSyncBusinessCommand
    : EvhBusinessCommand


@property(nonatomic, copy) NSNumber* userId;

// item type NSNumber*
@property(nonatomic, strong) NSMutableArray* namespaceIds;

@property(nonatomic, copy) NSNumber* scopeType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

