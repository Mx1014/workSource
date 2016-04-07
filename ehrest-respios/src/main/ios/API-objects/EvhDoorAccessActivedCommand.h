//
// EvhDoorAccessActivedCommand.h
// generated at 2016-04-07 14:16:29 
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

