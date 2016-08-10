//
// EvhListCommunityServiceResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhCommunityServiceDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListCommunityServiceResponse
//
@interface EvhListCommunityServiceResponse
    : NSObject<EvhJsonSerializable>


// item type EvhCommunityServiceDTO*
@property(nonatomic, strong) NSMutableArray* requests;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

