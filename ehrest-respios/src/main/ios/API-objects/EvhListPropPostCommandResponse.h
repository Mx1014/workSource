//
// EvhListPropPostCommandResponse.h
// generated at 2016-04-06 19:59:45 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPropertyPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPropPostCommandResponse
//
@interface EvhListPropPostCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageOffset;

// item type EvhPropertyPostDTO*
@property(nonatomic, strong) NSMutableArray* posts;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

