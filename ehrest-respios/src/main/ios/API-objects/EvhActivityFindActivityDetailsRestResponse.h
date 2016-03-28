//
// EvhActivityFindActivityDetailsRestResponse.h
// generated at 2016-03-25 19:05:21 
//
#import "RestResponseBase.h"
#import "EvhActivityListResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityFindActivityDetailsRestResponse
//
@interface EvhActivityFindActivityDetailsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhActivityListResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
