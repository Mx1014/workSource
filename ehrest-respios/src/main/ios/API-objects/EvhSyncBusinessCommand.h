//
// EvhSyncBusinessCommand.h
// generated at 2016-04-19 12:41:53 
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

@property(nonatomic, copy) NSNumber* namespaceId;

@property(nonatomic, copy) NSNumber* scopeType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

