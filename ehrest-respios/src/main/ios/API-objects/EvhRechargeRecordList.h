//
// EvhRechargeRecordList.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
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

