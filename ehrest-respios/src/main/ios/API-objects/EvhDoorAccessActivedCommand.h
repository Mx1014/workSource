//
// EvhDoorAccessActivedCommand.h
// generated at 2016-03-28 15:56:07 
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

