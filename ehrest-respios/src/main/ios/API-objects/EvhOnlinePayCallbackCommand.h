//
// EvhOnlinePayCallbackCommand.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhOnlinePayCallbackCommand
//
@interface EvhOnlinePayCallbackCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* payStatus;

@property(nonatomic, copy) NSString* vendorType;

@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSString* payTime;

@property(nonatomic, copy) NSString* payAmount;

@property(nonatomic, copy) NSString* payAccount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSString* payObj;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

