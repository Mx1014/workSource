//
// EvhPkgListRestResponse.h
// generated at 2016-04-05 13:45:27 
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
