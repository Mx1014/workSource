//
// EvhBaiduGeocoderResponse.h
// generated at 2016-04-29 18:56:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaiduResultDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaiduGeocoderResponse
//
@interface EvhBaiduGeocoderResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhBaiduResultDTO* result;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

