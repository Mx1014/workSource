//
// EvhBaiduResultDTO.h
// generated at 2016-04-19 12:41:53 
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

