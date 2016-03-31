//
// EvhBannerScope.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerScope
//
@interface EvhBannerScope
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* scopeCode;

@property(nonatomic, copy) NSNumber* scopeId;

@property(nonatomic, copy) NSNumber* order;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

