//
// EvhPkgAddRestResponse.h
// generated at 2016-03-30 10:13:09 
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
