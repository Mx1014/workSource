//
// EvhChannelList.h
// generated at 2016-03-25 11:43:34 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhMessageChannel.h"

///////////////////////////////////////////////////////////////////////////////
// EvhChannelList
//
@interface EvhChannelList
    : NSObject<EvhJsonSerializable>


// item type EvhMessageChannel*
@property(nonatomic, strong) NSMutableArray* channels;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

