//
// EvhTechparkEntryListForRentsRestResponse.h
//
#import "RestResponseBase.h"
#import "EvhListBuildingForRentResponse.h"

///////////////////////////////////////////////////////////////////////////////
// EvhTechparkEntryListForRentsRestResponse
//
@interface EvhTechparkEntryListForRentsRestResponse : EvhRestResponseBase

@property(nonatomic, strong) EvhListBuildingForRentResponse* response;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////
