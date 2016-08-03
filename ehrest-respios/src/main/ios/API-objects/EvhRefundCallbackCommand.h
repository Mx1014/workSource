//
// EvhRefundCallbackCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRefundCallbackCommand
//
@interface EvhRefundCallbackCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* appKey;

@property(nonatomic, copy) NSString* signature;

@property(nonatomic, copy) NSNumber* timestamp;

@property(nonatomic, copy) NSNumber* nonce;

@property(nonatomic, copy) NSString* crypto;

@property(nonatomic, copy) NSString* refundOrderNo;

@property(nonatomic, copy) NSString* onlinePayStyleNo;

@property(nonatomic, copy) NSString* orderNo;

@property(nonatomic, copy) NSString* orderType;

@property(nonatomic, copy) NSNumber* refundAmount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

