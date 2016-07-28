//
// EvhRentalv2FindRentalBillsCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalv2FindRentalBillsCommand
//
@interface EvhRentalv2FindRentalBillsCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* resourceTypeId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* billStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

