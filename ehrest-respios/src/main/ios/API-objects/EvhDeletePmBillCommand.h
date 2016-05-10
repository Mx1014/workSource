//
// EvhDeletePmBillCommand.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeletePmBillCommand
//
@interface EvhDeletePmBillCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* id;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

