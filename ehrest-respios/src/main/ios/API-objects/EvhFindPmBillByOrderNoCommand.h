//
// EvhFindPmBillByOrderNoCommand.h
// generated at 2016-03-28 15:56:07 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhFindPmBillByOrderNoCommand
//
@interface EvhFindPmBillByOrderNoCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* orderNo;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

