//
// EvhRegionDescriptor.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRegionDescriptor
//
@interface EvhRegionDescriptor
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* regionScope;

@property(nonatomic, copy) NSNumber* regionId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

