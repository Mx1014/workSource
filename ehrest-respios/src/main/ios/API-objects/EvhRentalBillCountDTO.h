//
// EvhRentalBillCountDTO.h
// generated at 2016-04-26 18:22:55 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRentalBillCountDTO
//
@interface EvhRentalBillCountDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* siteName;

@property(nonatomic, copy) NSNumber* sumCount;

@property(nonatomic, copy) NSNumber* completeCount;

@property(nonatomic, copy) NSNumber* cancelCount;

@property(nonatomic, copy) NSNumber* overTimeCount;

@property(nonatomic, copy) NSNumber* successCount;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

