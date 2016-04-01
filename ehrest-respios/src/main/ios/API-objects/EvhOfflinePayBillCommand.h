//
// EvhOfflinePayBillCommand.h
// generated at 2016-04-01 15:40:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOfflinePayBillCommand
//
@interface EvhOfflinePayBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* orderId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

