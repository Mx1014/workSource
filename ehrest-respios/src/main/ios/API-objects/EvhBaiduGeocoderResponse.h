//
// EvhBaiduGeocoderResponse.h
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

