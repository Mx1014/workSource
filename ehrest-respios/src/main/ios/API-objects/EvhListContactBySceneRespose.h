//
// EvhListContactBySceneRespose.h
// generated at 2016-04-06 19:10:41 
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

