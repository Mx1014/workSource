//
// EvhGetCurrentFirmwareCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCurrentFirmwareCommand
//
@interface EvhGetCurrentFirmwareCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* firmwareType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

