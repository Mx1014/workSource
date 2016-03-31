//
// EvhUpdatePmBillsCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUpdatePmBillsDto.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUpdatePmBillsCommand
//
@interface EvhUpdatePmBillsCommand
    : NSObject<EvhJsonSerializable>


// item type EvhUpdatePmBillsDto*
@property(nonatomic, strong) NSMutableArray* updateList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

