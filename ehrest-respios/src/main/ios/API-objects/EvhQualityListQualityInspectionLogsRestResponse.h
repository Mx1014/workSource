//
// EvhQualityListQualityInspectionLogsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListQualityInspectionLogsResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListQualityInspectionLogsRestResponse
//
@interface EvhQualityListQualityInspectionLogsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListQualityInspectionLogsResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
