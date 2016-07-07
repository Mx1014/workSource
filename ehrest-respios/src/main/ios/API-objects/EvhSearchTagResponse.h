//
// EvhSearchTagResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhTagDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSearchTagResponse
//
@interface EvhSearchTagResponse
    : NSObject<EvhJsonSerializable>


// item type EvhTagDTO*
@property(nonatomic, strong) NSMutableArray* tags;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

