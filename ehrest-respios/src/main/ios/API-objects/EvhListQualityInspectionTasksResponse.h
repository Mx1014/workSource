//
// EvhListQualityInspectionTasksResponse.h
// generated at 2016-04-19 13:40:01 
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

