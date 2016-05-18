//
// EvhPmsyBillsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmsyBillsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyBillsResponse
//
@interface EvhPmsyBillsResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* customerId;

@property(nonatomic, copy) NSString* projectId;

@property(nonatomic, copy) NSString* resourceId;

@property(nonatomic, copy) NSNumber* payerId;

@property(nonatomic, copy) NSNumber* monthCount;

@property(nonatomic, copy) NSNumber* totalAmount;

@property(nonatomic, copy) NSString* billTip;

@property(nonatomic, copy) NSString* contact;

// item type EvhPmsyBillsDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

