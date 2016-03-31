//
// EvhRentalBillCountDTO.h
// generated at 2016-03-31 19:08:52 
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

