//
// EvhListNewsCommentResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhNewsCommentDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListNewsCommentResponse
//
@interface EvhListNewsCommentResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhNewsCommentDTO*
@property(nonatomic, strong) NSMutableArray* commentList;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

