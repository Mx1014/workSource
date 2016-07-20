//
// EvhBaiduResultDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhBaiduAddressComponentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBaiduResultDTO
//
@interface EvhBaiduResultDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, strong) EvhBaiduAddressComponentDTO* addressComponent;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

