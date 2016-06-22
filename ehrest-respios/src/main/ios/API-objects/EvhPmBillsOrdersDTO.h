//
// EvhPmBillsOrdersDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmBillsOrdersDTO
//
@interface EvhPmBillsOrdersDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* payerUid;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userContact;

@property(nonatomic, copy) NSNumber* orderAmount;

@property(nonatomic, copy) NSNumber* paidTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* paidType;

@property(nonatomic, copy) NSNumber* billDate;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

