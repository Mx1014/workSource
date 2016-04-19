//
// EvhWaitingLine.h
// generated at 2016-04-19 14:25:57 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWaitingLine
//
@interface EvhWaitingLine
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* waitingPeople;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

