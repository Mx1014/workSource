//
// EvhPmsyBillsDTO.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPmsyBillItemDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPmsyBillsDTO
//
@interface EvhPmsyBillsDTO
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* billDateStr;

@property(nonatomic, copy) NSNumber* monthlyReceivableAmount;

@property(nonatomic, copy) NSNumber* monthlyDebtAmount;

// item type EvhPmsyBillItemDTO*
@property(nonatomic, strong) NSMutableArray* requests;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

