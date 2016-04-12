//
// EvhLinkFindLinkByIdRestResponse.h
// generated at 2016-04-08 20:09:23 
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
