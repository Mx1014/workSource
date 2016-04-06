//
// EvhPkgListRestResponse.h
// generated at 2016-04-06 19:10:43 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPkgListRestResponse
//
@interface EvhPkgListRestResponse : EvhRestResponseBase

// array of EvhClientPackageFileDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
