//
// EvhImageConfig.h
// generated at 2016-04-05 13:45:25 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhImageConfig
//
@interface EvhImageConfig
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* width;

@property(nonatomic, copy) NSNumber* height;

@property(nonatomic, copy) NSNumber* gary;

@property(nonatomic, copy) NSNumber* proportion;

@property(nonatomic, copy) NSNumber* rotate;

@property(nonatomic, copy) NSString* format;

@property(nonatomic, copy) NSNumber* quality;

@property(nonatomic, copy) NSNumber* x;

@property(nonatomic, copy) NSNumber* y;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

