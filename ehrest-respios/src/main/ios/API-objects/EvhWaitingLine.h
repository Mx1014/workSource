//
// EvhWaitingLine.h
// generated at 2016-03-25 17:08:10 
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

