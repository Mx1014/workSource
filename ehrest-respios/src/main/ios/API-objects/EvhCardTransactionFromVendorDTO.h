//
// EvhCardTransactionFromVendorDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardTransactionFromVendorDTO
//
@interface EvhCardTransactionFromVendorDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* cardNo;

@property(nonatomic, copy) NSNumber* orderNo;

@property(nonatomic, copy) NSNumber* consumeType;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* transactionTime;

@property(nonatomic, copy) NSString* status;

@property(nonatomic, copy) NSString* merchant;

@property(nonatomic, copy) NSString* transactionType;

@property(nonatomic, copy) NSString* vendorName;

@property(nonatomic, copy) NSString* vendorResult;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

