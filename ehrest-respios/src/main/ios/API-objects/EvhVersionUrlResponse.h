//
// EvhVersionUrlResponse.h
// generated at 2016-03-28 15:56:08 
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

