//
// EvhActivityFindActivityDetailsRestResponse.h
// generated at 2016-03-28 15:56:09 
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
