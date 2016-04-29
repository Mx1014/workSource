//
// EvhCreatePmsyBillOrderCommand.h
// generated at 2016-04-29 18:56:03 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatePmsyBillOrderCommand
//
@interface EvhCreatePmsyBillOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* projectId;

@property(nonatomic, copy) NSString* customerId;

@property(nonatomic, copy) NSString* resourceId;

@property(nonatomic, copy) NSNumber* creatorId;

@property(nonatomic, copy) NSNumber* orderAmount;

@property(nonatomic, copy) NSString* paidType;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* billIds;

@property(nonatomic, copy) NSNumber* pmPayerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

