//
// EvhVersionUrlResponse.h
// generated at 2016-04-26 18:22:55 
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

