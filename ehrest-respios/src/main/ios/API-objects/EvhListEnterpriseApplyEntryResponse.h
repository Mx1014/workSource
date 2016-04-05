//
// EvhListEnterpriseApplyEntryResponse.h
// generated at 2016-04-05 13:45:26 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhEnterpriseApplyEntryDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListEnterpriseApplyEntryResponse
//
@interface EvhListEnterpriseApplyEntryResponse
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* nextPageAnchor;

// item type EvhEnterpriseApplyEntryDTO*
@property(nonatomic, strong) NSMutableArray* entrys;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

