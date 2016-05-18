//
// EvhListQualityInspectionTasksResponse.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhQualityInspectionTaskDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListQualityInspectionTasksResponse
//
@interface EvhListQualityInspectionTasksResponse
    : NSObject<EvhJsonSerializable>


// item type EvhQualityInspectionTaskDTO*
@property(nonatomic, strong) NSMutableArray* tasks;

@property(nonatomic, copy) NSNumber* nextPageAnchor;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

