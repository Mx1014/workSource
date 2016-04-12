//
// EvhIncompleteBillCommand.h
// generated at 2016-04-08 20:09:22 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhIncompleteBillCommand
//
@interface EvhIncompleteBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerType;

@property(nonatomic, copy) NSNumber* ownerId;

@property(nonatomic, copy) NSString* siteType;

@property(nonatomic, copy) NSNumber* rentalBillId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

