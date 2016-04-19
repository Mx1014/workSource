//
// EvhPkgAddRestResponse.h
// generated at 2016-04-19 12:41:55 
//
#import "RestResponseBase.h"
#import "EvhClientPackageFileDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPkgAddRestResponse
//
@interface EvhPkgAddRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhClientPackageFileDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
