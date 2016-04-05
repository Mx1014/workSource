//
// EvhDeleteHistoryByIdCommand.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhDeleteHistoryByIdCommand
//
@interface EvhDeleteHistoryByIdCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* historyId;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

