//
// EvhActivityFindActivityDetailsRestResponse.h
// generated at 2016-04-19 12:41:54 
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
