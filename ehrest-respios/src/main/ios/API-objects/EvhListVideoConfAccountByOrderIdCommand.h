//
// EvhListVideoConfAccountByOrderIdCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListVideoConfAccountByOrderIdCommand
//
@interface EvhListVideoConfAccountByOrderIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orderId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

