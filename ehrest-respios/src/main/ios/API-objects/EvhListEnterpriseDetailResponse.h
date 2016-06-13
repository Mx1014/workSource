//
// EvhListEnterpriseDetailResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseDetailDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseDetailResponse
//
@interface EvhListEnterpriseDetailResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseDetailDTO*
@property(nonatomic, strong) NSMutableArray* details;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

