//
// EvhRegionDescriptor.h
// generated at 2016-03-28 15:56:08 
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

