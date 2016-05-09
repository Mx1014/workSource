//
// EvhSearchBillsOrdersCommand.h
// generated at 2016-04-29 18:56:00 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchBillsOrdersCommand
//
@interface EvhSearchBillsOrdersCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* startDate;

@property(nonatomic, copy) NSNumber* endDate;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userContact;

@property(nonatomic, copy) NSNumber* pageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

