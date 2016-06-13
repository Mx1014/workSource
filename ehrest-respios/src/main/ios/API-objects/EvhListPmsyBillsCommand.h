//
// EvhListPmsyBillsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmsyBillType.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPmsyBillsCommand
//
@interface EvhListPmsyBillsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* customerId;

@property(nonatomic, copy) NSString* projectId;

@property(nonatomic, copy) NSString* resourceId;

@property(nonatomic, copy) NSNumber* startDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSNumber* payerId;

@property(nonatomic, assign) EvhPmsyBillType billType;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

