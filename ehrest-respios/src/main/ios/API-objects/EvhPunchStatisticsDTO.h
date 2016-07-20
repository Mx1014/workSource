//
// EvhPunchStatisticsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPunchStatisticsDTO
//
@interface EvhPunchStatisticsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* userId;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userEnterpriseGroup;

@property(nonatomic, copy) NSString* userDepartment;

@property(nonatomic, copy) NSString* userPhoneNumber;

@property(nonatomic, copy) NSNumber* enterpriseId;

@property(nonatomic, copy) NSNumber* punchDate;

@property(nonatomic, copy) NSNumber* arriveTime;

@property(nonatomic, copy) NSNumber* noonLeaveTime;

@property(nonatomic, copy) NSNumber* afternoonArriveTime;

@property(nonatomic, copy) NSNumber* leaveTime;

@property(nonatomic, copy) NSNumber* workTime;

@property(nonatomic, copy) NSNumber* status;

@property(nonatomic, copy) NSNumber* morningStatus;

@property(nonatomic, copy) NSNumber* afternoonStatus;

@property(nonatomic, copy) NSNumber* creatorUid;

@property(nonatomic, copy) NSNumber* createTime;

@property(nonatomic, copy) NSString* operatorName;

@property(nonatomic, copy) NSNumber* approvalStatus;

@property(nonatomic, copy) NSNumber* morningApprovalStatus;

@property(nonatomic, copy) NSNumber* afternoonApprovalStatus;

@property(nonatomic, copy) NSNumber* viewFlag;

@property(nonatomic, copy) NSNumber* punchTimesPerDay;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

