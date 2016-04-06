//
// EvhPkgGetUpgradeFileInfoRestResponse.h
// generated at 2016-04-06 19:59:47 
//
#import "RestResponseBase.h"

///////////////////////////////////////////////////////////////////////////////
// EvhPkgGetUpgradeFileInfoRestResponse
//
@interface EvhPkgGetUpgradeFileInfoRestResponse : EvhRestResponseBase

// array of EvhClientPackageFileDTO* objects
@property(nonatomic, strong) NSMutableArray* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
