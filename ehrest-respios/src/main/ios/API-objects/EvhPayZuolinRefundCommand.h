//
// EvhPayZuolinRefundCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPayZuolinRefundCommand
//
@interface EvhPayZuolinRefundCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* signature;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* nonce;

@property(nonatomic, copy) NSString* crypto;

@property(nonatomic, copy) NSString* refundOrderNo;

@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* onlinePayStyleNo;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSNumber* refundAmount;

@property(nonatomic, copy) NSString* refundMsg;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

