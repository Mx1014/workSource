//
// EvhPayCallbackCommand.h
// generated at 2016-04-18 14:48:51 
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

