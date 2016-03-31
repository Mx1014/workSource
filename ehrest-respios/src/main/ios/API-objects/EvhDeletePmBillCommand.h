//
// EvhDeletePmBillCommand.h
// generated at 2016-03-28 15:56:09 
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

