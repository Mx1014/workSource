//
// EvhListPostCommandResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPostCommandResponse
//
@interface EvhListPostCommandResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhPostDTO*
@property(nonatomic, strong) NSMutableArray* posts;

@property(nonatomic, copy) NSString* keywords;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

