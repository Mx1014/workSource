//
// EvhWaitingLine.h
// generated at 2016-03-25 15:57:23 
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

