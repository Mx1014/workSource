//
// EvhGetResourceTypeListResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhResourceTypeDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetResourceTypeListResponse
//
@interface EvhGetResourceTypeListResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhResourceTypeDTO*
@property(nonatomic, strong) NSMutableArray* resourceTypes;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

