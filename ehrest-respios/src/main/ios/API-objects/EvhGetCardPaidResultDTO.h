//
// EvhGetCardPaidResultDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetCardPaidResultDTO
//
@interface EvhGetCardPaidResultDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* token;

@property(nonatomic, copy) NSString* merchantNo;

@property(nonatomic, copy) NSString* merchantName;

@property(nonatomic, copy) NSNumber* amount;

@property(nonatomic, copy) NSNumber* disAmount;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* transactionTime;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

