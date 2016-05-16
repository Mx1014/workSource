//
// EvhQualityListRecordsByTaskIdRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQualityListRecordsByTaskIdRestResponse
//
@interface EvhQualityListRecordsByTaskIdRestResponse : EvhRestResponseBase

// array of EvhQualityInspectionTaskRecordsDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
