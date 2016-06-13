//
// EvhAdminScheduleListJobInfosRestResponse.h
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminScheduleListJobInfosRestResponse
//
@interface EvhAdminScheduleListJobInfosRestResponse : EvhRestResponseBase

// array of EvhScheduleJobInfoDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
