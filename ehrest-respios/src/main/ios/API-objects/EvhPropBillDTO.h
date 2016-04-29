//
// EvhPropBillDTO.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:53 
>>>>>>> 3.3.x
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

