//
// EvhWaitingLine.h
// generated at 2016-04-07 10:47:31 
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

