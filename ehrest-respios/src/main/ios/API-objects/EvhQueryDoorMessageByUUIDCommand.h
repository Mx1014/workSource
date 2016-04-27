//
// EvhQueryDoorMessageByUUIDCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorMessageByUUIDCommand
//
@interface EvhQueryDoorMessageByUUIDCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

// item type EvhDoorMessage*
@property(nonatomic, strong) NSMutableArray* inputs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

