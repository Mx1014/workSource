//
// EvhPkgAddRestResponse.h
// generated at 2016-04-07 17:33:50 
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
