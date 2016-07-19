//
// EvhListCommunityByNamespaceIdResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunityByNamespaceIdResponse
//
@interface EvhListCommunityByNamespaceIdResponse
    : NSObject<EvhJsonSerializable>


// item type EvhCommunityDTO*
@property(nonatomic, strong) NSMutableArray* communities;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

