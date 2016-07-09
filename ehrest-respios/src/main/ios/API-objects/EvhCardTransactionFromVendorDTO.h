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

