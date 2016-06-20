//
// EvhCardTransactionDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCardTransactionDTO
//
@interface EvhCardTransactionDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* payerUid;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* mobile;

@property(nonatomic, copy) NSString* itemName;

@property(nonatomic, copy) NSString* merchant;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSString* transactionNo;

@property(nonatomic, copy) NSNumber* transactionTime;

@property(nonatomic, copy) NSNumber* cardId;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* vendorName;

@property(nonatomic, copy) NSString* vendorResult;

@property(nonatomic, copy) NSNumber* comsumeType;

@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSString* cardNo;

@property(nonatomic, copy) NSNumber* orderNo;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

