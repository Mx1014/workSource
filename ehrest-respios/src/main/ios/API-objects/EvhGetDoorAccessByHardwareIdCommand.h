//
// EvhGetDoorAccessByHardwareIdCommand.h
// generated at 2016-04-12 15:02:20 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetDoorAccessByHardwareIdCommand
//
@interface EvhGetDoorAccessByHardwareIdCommand
    : NSObject<EvhJsonSerializable>


// item type NSString*
@property(nonatomic, strong) NSMutableArray* hardwareIds;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

