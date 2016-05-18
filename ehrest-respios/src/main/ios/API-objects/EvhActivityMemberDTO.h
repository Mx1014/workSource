//
// EvhActivityMemberDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityMemberDTO
//
@interface EvhActivityMemberDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

@property(nonatomic, copy) NSNumber* uid;

@property(nonatomic, copy) NSString* userName;

@property(nonatomic, copy) NSString* userAvatar;

@property(nonatomic, copy) NSString* familyName;

@property(nonatomic, copy) NSNumber* familyId;

@property(nonatomic, copy) NSNumber* adultCount;

@property(nonatomic, copy) NSNumber* childCount;

@property(nonatomic, copy) NSNumber* checkinFlag;

@property(nonatomic, copy) NSString* checkinTime;

@property(nonatomic, copy) NSNumber* confirmFlag;

@property(nonatomic, copy) NSString* confirmTime;

@property(nonatomic, copy) NSNumber* creatorFlag;

@property(nonatomic, copy) NSNumber* lotteryWinnerFlag;

@property(nonatomic, copy) NSString* lotteryWonTime;

// item type NSString*
@property(nonatomic, strong) NSMutableArray* phone;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

