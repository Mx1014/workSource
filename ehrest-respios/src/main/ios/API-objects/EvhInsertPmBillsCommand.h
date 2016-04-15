//
// EvhInsertPmBillsCommand.h
// generated at 2016-04-12 15:02:19 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhUpdatePmBillsDto.h"

///////////////////////////////////////////////////////////////////////////////
// EvhInsertPmBillsCommand
//
@interface EvhInsertPmBillsCommand
    : NSObject<EvhJsonSerializable>


// item type EvhUpdatePmBillsDto*
@property(nonatomic, strong) NSMutableArray* insertList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

