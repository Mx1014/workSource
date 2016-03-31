//
// EvhRechargeRecordList.h
// generated at 2016-03-31 15:43:21 
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

