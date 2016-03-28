//
// EvhPkgAddRestResponse.h
// generated at 2016-03-28 15:56:09 
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
