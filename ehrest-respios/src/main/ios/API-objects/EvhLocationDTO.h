//
// EvhLocationDTO.h
// generated at 2016-04-19 12:41:53 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLocationDTO
//
@interface EvhLocationDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* latitude;

@property(nonatomic, copy) NSNumber* longitude;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

