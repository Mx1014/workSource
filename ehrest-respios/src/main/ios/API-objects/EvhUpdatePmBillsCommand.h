//
// EvhUpdatePmBillsCommand.h
// generated at 2016-04-05 13:45:25 
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

