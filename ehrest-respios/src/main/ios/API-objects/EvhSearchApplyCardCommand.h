//
// EvhSearchApplyCardCommand.h
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

