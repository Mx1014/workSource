//
// EvhListContactRespose.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhContactDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListContactRespose
//
@interface EvhListContactRespose
    : NSObject<EvhJsonSerializable>


// item type EvhContactDTO*
@property(nonatomic, strong) NSMutableArray* contacts;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

