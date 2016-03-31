//
// EvhCreatePmBillOrderCommand.h
// generated at 2016-03-31 13:49:14 
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

