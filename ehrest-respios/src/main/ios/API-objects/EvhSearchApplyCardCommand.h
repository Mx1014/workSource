//
// EvhSearchApplyCardCommand.h
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchApplyCardCommand
//
@interface EvhSearchApplyCardCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* applyStatus;

@property(nonatomic, copy) NSString* applierName;

@property(nonatomic, copy) NSString* applierPhone;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSString* beginDay;

@property(nonatomic, copy) NSString* endDay;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

