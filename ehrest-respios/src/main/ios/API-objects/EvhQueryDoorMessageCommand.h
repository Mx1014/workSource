//
// EvhQueryDoorMessageCommand.h
// generated at 2016-03-25 15:57:21 
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

