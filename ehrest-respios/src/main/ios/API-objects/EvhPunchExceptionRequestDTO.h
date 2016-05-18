//
// EvhPunchExceptionRequestDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchExceptionRequestDTO
//
@interface EvhPunchExceptionRequestDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userPhoneNumber;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* punchDate;

@property(nonatomic, copy) NSNumber* requestType;

@property(nonatomic, copy) NSString* description_;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* approvalStatus;

@property(nonatomic, copy) NSNumber* morningApprovalStatus;

@property(nonatomic, copy) NSNumber* afternoonApprovalStatus;

@property(nonatomic, copy) NSNumber* processCode;

@property(nonatomic, copy) NSString* processDetails;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSNumber* operatorUid;

@property(nonatomic, copy) NSString* operatorName;

@property(nonatomic, copy) NSNumber* operateTime;

@property(nonatomic, copy) NSNumber* punchStatus;

@property(nonatomic, copy) NSNumber* morningPunchStatus;

@property(nonatomic, copy) NSNumber* afternoonPunchStatus;

@property(nonatomic, copy) NSNumber* arriveTime;

@property(nonatomic, copy) NSNumber* leaveTime;

@property(nonatomic, copy) NSNumber* workTime;

@property(nonatomic, copy) NSNumber* noonLeaveTime;

@property(nonatomic, copy) NSNumber* afternoonArriveTime;

@property(nonatomic, copy) NSNumber* punchTimesPerDay;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

