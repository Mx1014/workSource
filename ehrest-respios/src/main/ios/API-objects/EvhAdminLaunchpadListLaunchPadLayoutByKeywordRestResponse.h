//
// EvhAdminLaunchpadListLaunchPadLayoutByKeywordRestResponse.h
// generated at 2016-04-12 15:02:20 
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
