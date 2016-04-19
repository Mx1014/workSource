//
// EvhDoorAccessActivedCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

