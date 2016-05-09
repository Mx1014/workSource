//
// EvhListQualityStandardsResponse.h
// generated at 2016-04-29 18:56:01 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityStandardsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityStandardsResponse
//
@interface EvhListQualityStandardsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhQualityStandardsDTO*
@property(nonatomic, strong) NSMutableArray* qaStandards;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

