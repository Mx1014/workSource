//
// EvhUiOrgProcessingTaskRestResponse.h
// generated at 2016-04-07 14:16:32 
//
#import "RestResponseBase.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhUiOrgProcessingTaskRestResponse
//
@interface EvhUiOrgProcessingTaskRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
