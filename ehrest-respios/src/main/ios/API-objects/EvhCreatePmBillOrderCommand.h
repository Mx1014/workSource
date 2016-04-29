//
// EvhCreatePmBillOrderCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

