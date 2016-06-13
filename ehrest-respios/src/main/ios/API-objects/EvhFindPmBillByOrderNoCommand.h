//
// EvhFindPmBillByOrderNoCommand.h
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

