//
// EvhDoorAccessActivedCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDoorAccessActivedCommand
//
@interface EvhDoorAccessActivedCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* doorId;

@property(nonatomic, copy) NSString* hardwareId;

@property(nonatomic, copy) NSNumber* time;

@property(nonatomic, copy) NSString* content;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

