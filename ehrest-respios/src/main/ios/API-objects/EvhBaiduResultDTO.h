//
// EvhBaiduResultDTO.h
// generated at 2016-04-12 19:00:51 
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

