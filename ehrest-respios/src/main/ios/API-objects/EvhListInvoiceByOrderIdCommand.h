//
// EvhListInvoiceByOrderIdCommand.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListInvoiceByOrderIdCommand
//
@interface EvhListInvoiceByOrderIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orderId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

