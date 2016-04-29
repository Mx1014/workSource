//
// EvhPayCallbackCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayCallbackCommand
//
@interface EvhPayCallbackCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* payStatus;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSString* vendorType;

@property(nonatomic, copy) NSString* payTime;

@property(nonatomic, copy) NSString* payAmount;

@property(nonatomic, copy) NSString* payAccount;

@property(nonatomic, copy) NSString* payObj;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

