//
// EvhDeleteHistoryByIdCommand.h
// generated at 2016-04-22 13:56:49 
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

