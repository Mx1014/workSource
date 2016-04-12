//
// EvhDeleteHistoryByIdCommand.h
// generated at 2016-04-08 20:09:21 
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

