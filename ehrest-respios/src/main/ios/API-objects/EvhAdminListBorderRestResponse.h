//
// EvhAdminListBorderRestResponse.h
// generated at 2016-04-26 18:22:56 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminListBorderRestResponse
//
@interface EvhAdminListBorderRestResponse : EvhRestResponseBase

// array of EvhBorderDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
