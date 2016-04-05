//
// EvhCreatePmBillOrderCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatePmBillOrderCommand
//
@interface EvhCreatePmBillOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billId;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* description_;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

