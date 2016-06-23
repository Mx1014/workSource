//
// EvhNewsCreateNewsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhBriefNewsDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhNewsCreateNewsRestResponse
//
@interface EvhNewsCreateNewsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBriefNewsDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
