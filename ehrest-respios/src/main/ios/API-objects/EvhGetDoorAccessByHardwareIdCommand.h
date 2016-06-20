//
// EvhGetDoorAccessByHardwareIdCommand.h
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

@property(nonatomic, copy) NSNumber* organizationId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

