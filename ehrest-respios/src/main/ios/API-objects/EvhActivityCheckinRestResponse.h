//
// EvhActivityCheckinRestResponse.h
// generated at 2016-03-31 13:49:15 
//
#import "RestResponseBase.h"
#import "EvhActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivityCheckinRestResponse
//
@interface EvhActivityCheckinRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhActivityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
