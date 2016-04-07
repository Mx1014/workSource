//
// EvhListTaskPostsResponse.h
// generated at 2016-04-07 17:03:16 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListTaskPostsResponse
//
@interface EvhListTaskPostsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhPostDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

