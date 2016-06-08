//
// EvhListPostResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListPostResponse
//
@interface EvhListPostResponse
    : NSObject<EvhJsonSerializable>


// item type EvhPostDTO*
@property(nonatomic, strong) NSMutableArray* postDtos;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

