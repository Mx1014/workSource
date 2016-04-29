//
// EvhlistBuildingsByStatusCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhlistBuildingsByStatusCommand
//
@interface EvhlistBuildingsByStatusCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* status;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

