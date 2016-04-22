//
// EvhBusinessFindBusinessByIdRestResponse.h
// generated at 2016-04-22 13:56:50 
//
#import "RestResponseBase.h"
#import "EvhBusinessDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessFindBusinessByIdRestResponse
//
@interface EvhBusinessFindBusinessByIdRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhBusinessDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
