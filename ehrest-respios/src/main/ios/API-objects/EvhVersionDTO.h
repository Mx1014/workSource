//
// EvhVersionDTO.h
// generated at 2016-03-25 19:05:21 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionDTO
//
@interface EvhVersionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* major;

@property(nonatomic, copy) NSNumber* minor;

@property(nonatomic, copy) NSNumber* revision;

@property(nonatomic, copy) NSString* tag;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

