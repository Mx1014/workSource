//
// EvhQueryDoorMessageCommand.h
// generated at 2016-04-08 20:09:23 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhDoorMessage.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryDoorMessageCommand
//
@interface EvhQueryDoorMessageCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSNumber* urgent;

// item type EvhDoorMessage*
@property(nonatomic, strong) NSMutableArray* inputs;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

