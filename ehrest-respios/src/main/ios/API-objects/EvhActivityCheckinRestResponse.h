//
// EvhActivityCheckinRestResponse.h
// generated at 2016-04-26 18:22:56 
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
