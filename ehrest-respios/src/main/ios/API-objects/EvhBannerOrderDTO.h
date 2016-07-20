//
// EvhBannerOrderDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerOrderDTO
//
@interface EvhBannerOrderDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* bannerId;

@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSString* vendorOrderTag;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* purchaseTime;

@property(nonatomic, copy) NSNumber* createTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

