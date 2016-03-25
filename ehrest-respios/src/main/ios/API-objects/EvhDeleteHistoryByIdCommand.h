//
// EvhDeleteHistoryByIdCommand.h
// generated at 2016-03-25 09:26:41 
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

