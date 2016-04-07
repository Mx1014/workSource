//
// EvhSyncDeleteBusinessCommand.h
// generated at 2016-04-07 14:16:31 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSyncDeleteBusinessCommand
//
@interface EvhSyncDeleteBusinessCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* id;

@property(nonatomic, copy) NSNumber* userId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

