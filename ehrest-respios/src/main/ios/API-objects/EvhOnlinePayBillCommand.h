//
// EvhOnlinePayBillCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:50 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOnlinePayBillCommand
//
@interface EvhOnlinePayBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* payStatus;

@property(nonatomic, copy) NSString* vendorType;

@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* payTime;

@property(nonatomic, copy) NSString* payAmount;

@property(nonatomic, copy) NSString* payAccount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* payObj;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

