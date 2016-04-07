//
// EvhRechargeRecordList.h
// generated at 2016-04-07 17:57:42 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhRechargeRecordDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRechargeRecordList
//
@interface EvhRechargeRecordList
    : NSObject<EvhJsonSerializable>


// item type EvhRechargeRecordDTO*
@property(nonatomic, strong) NSMutableArray* rechargeRecord;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

