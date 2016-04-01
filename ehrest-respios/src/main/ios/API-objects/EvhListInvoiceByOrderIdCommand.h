//
// EvhListInvoiceByOrderIdCommand.h
// generated at 2016-03-31 20:15:32 
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

