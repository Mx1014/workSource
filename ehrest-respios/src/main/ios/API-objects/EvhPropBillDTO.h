//
// EvhPropBillDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPropBillDTO
//
@interface EvhPropBillDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSString* entityType;

@property(nonatomic, copy) NSNumber* entityId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSString* name;

@property(nonatomic, copy) NSString* dateStr;

@property(nonatomic, copy) NSNumber* totalAmount;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* notifyCount;

@property(nonatomic, copy) NSNumber* notifyTime;

// item type EvhPropBillItemDTO*
@property(nonatomic, strong) NSMutableArray* itemList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

