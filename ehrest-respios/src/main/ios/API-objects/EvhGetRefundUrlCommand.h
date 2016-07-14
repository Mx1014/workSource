//
// EvhGetRefundUrlCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetRefundUrlCommand
//
@interface EvhGetRefundUrlCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* refundId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

