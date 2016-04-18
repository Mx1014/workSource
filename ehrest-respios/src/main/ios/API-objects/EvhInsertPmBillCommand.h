//
// EvhInsertPmBillCommand.h
// generated at 2016-04-18 14:48:51 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInsertPmBillCommand
//
@interface EvhInsertPmBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* organizationId;

@property(nonatomic, copy) NSString* address;

@property(nonatomic, copy) NSNumber* startDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSNumber* payDate;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* dueAmount;

@property(nonatomic, copy) NSNumber* oweAmount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

