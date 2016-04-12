//
// EvhListPostCommandResponse.h
// generated at 2016-04-08 20:09:22 
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

