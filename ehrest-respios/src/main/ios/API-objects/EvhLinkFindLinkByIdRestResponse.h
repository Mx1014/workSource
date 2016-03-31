//
// EvhLinkFindLinkByIdRestResponse.h
// generated at 2016-03-31 10:18:21 
//
#import "RestResponseBase.h"
#import "EvhLinkDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhLinkFindLinkByIdRestResponse
//
@interface EvhLinkFindLinkByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLinkDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
