//
// EvhActivitySignupRestResponse.h
// generated at 2016-04-05 13:45:26 
//
#import "RestResponseBase.h"
#import "EvhActivityDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhActivitySignupRestResponse
//
@interface EvhActivitySignupRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhActivityDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
