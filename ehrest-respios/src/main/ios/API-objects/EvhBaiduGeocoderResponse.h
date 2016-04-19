//
// EvhBaiduGeocoderResponse.h
// generated at 2016-04-19 14:25:57 
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

