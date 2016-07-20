//
// EvhListWifiSettingResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhWifiSettingDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListWifiSettingResponse
//
@interface EvhListWifiSettingResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhWifiSettingDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

