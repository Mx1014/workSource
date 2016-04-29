//
// EvhListContactBySceneRespose.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSceneContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactBySceneRespose
//
@interface EvhListContactBySceneRespose
    : NSObject<EvhJsonSerializable>


// item type EvhSceneContactDTO*
@property(nonatomic, strong) NSMutableArray* contacts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

