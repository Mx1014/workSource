//
// EvhRechargeRecordList.h
// generated at 2016-04-08 20:09:23 
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

