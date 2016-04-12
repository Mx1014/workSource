//
// EvhListEnterpriseApplyEntryResponse.h
// generated at 2016-04-08 20:09:21 
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

