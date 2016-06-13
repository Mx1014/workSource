//
// EvhOpPromotionSearchCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOpPromotionSearchCommand
//
@interface EvhOpPromotionSearchCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* keyword;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

