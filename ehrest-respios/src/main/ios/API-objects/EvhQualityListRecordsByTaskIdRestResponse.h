//
// EvhQualityListRecordsByTaskIdRestResponse.h
// generated at 2016-04-22 13:56:51 
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
