//
// EvhOpPromotionWebPageData.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionWebPageData
//
@interface EvhOpPromotionWebPageData
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* url;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

