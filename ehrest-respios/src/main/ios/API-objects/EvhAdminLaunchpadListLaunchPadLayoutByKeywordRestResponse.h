//
// EvhAdminLaunchpadListLaunchPadLayoutByKeywordRestResponse.h
// generated at 2016-03-31 20:15:33 
//
#import "RestResponseBase.h"
#import "EvhLaunchPadLayoutDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAdminLaunchpadListLaunchPadLayoutByKeywordRestResponse
//
@interface EvhAdminLaunchpadListLaunchPadLayoutByKeywordRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhLaunchPadLayoutDTO* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
