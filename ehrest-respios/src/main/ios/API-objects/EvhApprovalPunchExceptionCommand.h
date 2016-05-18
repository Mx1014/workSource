//
// EvhApprovalPunchExceptionCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhApprovalPunchExceptionCommand
//
@interface EvhApprovalPunchExceptionCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSString* punchDate;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* morningApprovalStatus;

@property(nonatomic, copy) NSNumber* afternoonApprovalStatus;

@property(nonatomic, copy) NSNumber* approvalStatus;

@property(nonatomic, copy) NSString* processDetails;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSNumber* punchTimesPerDay;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

