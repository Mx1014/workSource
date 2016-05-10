//
// EvhCreatePmsyBillOrderCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCreatePmsyBillOrderCommand
//
@interface EvhCreatePmsyBillOrderCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSString* projectId;

@property(nonatomic, copy) NSString* customerId;

@property(nonatomic, copy) NSString* resourceId;

@property(nonatomic, copy) NSNumber* orderAmount;

@property(nonatomic, copy) NSString* paidType;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* billIds;

@property(nonatomic, copy) NSNumber* pmPayerId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

