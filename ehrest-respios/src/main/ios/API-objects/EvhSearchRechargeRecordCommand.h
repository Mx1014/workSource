//
// EvhSearchRechargeRecordCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchRechargeRecordCommand
//
@interface EvhSearchRechargeRecordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* ownerName;

@property(nonatomic, copy) NSString* rechargePhone;

@property(nonatomic, copy) NSString* plateNumber;

@property(nonatomic, copy) NSNumber* communityId;

@property(nonatomic, copy) NSNumber* pageAnchor;

@property(nonatomic, copy) NSNumber* pageSize;

@property(nonatomic, copy) NSNumber* startTime;

@property(nonatomic, copy) NSNumber* endTime;

@property(nonatomic, copy) NSNumber* rechargeStatus;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

