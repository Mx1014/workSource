//
// EvhRentalBillCountDTO.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
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

