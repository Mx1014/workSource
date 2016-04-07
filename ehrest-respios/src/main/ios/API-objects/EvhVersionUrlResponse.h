//
// EvhVersionUrlResponse.h
// generated at 2016-04-07 10:47:32 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhVersionUrlResponse
//
@interface EvhVersionUrlResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* downloadUrl;

@property(nonatomic, copy) NSString* infoUrl;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

