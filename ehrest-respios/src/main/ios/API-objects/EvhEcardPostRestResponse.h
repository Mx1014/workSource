//
// EvhEcardPostRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"
#import "EvhPostDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhEcardPostRestResponse
//
@interface EvhEcardPostRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhPostDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
