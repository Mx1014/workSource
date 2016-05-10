//
// EvhAdminLaunchpadListLaunchPadLayoutByKeywordRestResponse.h
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
