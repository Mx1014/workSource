//
// EvhAdminLaunchpadListLaunchPadLayoutByKeywordRestResponse.h
// generated at 2016-04-07 17:33:49 
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
