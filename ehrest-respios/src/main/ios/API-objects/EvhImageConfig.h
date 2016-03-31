//
// EvhImageConfig.h
// generated at 2016-03-31 13:49:13 
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

