//
// EvhListQualityInspectionLogsResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityInspectionLogDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityInspectionLogsResponse
//
@interface EvhListQualityInspectionLogsResponse
    : NSObject<EvhJsonSerializable>


// item type EvhQualityInspectionLogDTO*
@property(nonatomic, strong) NSMutableArray* dtos;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

